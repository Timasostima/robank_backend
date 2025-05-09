package dev.tymurkulivar.robank_api.entities;

import dev.tymurkulivar.robank_api.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    private RobankUser user;

    public Category(CategoryDTO categoryDTO) {
        this.name = categoryDTO.getName();
        this.color = categoryDTO.getColor();
    }
}
