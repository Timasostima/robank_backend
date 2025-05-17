package dev.tymurkulivar.robank_backend.dto;

import dev.tymurkulivar.robank_backend.entities.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoalDTO {
    private Long id; // Add id field
    private String name;
    private Double price;
    private Integer index;

    public GoalDTO(Goal goal) {
        this.id = goal.getId();
        this.name = goal.getName();
        this.price = goal.getPrice();
        this.index = goal.getIndex();
    }
}