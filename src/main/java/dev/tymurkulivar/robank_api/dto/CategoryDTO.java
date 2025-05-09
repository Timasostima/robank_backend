package dev.tymurkulivar.robank_api.dto;

import dev.tymurkulivar.robank_api.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String color;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.color = category.getColor();
    }
}
