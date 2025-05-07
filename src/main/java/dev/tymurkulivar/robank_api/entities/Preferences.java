package dev.tymurkulivar.robank_api.entities;

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
@Table(name = "preferences")
public class Preferences {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "preferences", cascade = CascadeType.ALL)
    private RobankUser user;

    private String language;
    private String currency;
    private String theme;
    private Boolean notifications;

    public Preferences(String language, String currency, String theme, Boolean notifications) {
        this.language = language;
        this.currency = currency;
        this.theme = theme;
        this.notifications = notifications;
    }
}