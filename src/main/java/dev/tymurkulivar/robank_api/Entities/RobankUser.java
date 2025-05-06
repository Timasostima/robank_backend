package dev.tymurkulivar.robank_api.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class RobankUser {

    @Id
    private String uid;
    private String email;
    private String name;
    private String pictureUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preferences_id")
    private Preferences preferences;

    public RobankUser(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.pictureUrl = null;
    }

    @PrePersist
    private void initializePreferences() {
        if (this.preferences == null) {
            this.preferences = new Preferences("system", "eur", "system", true);
        }
    }
}