package recipes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipes.models.dto.RecipeDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Recipe {
    private static Long count = 1L;
    @Column
    @Id
    @JsonIgnore
    private Long id;
    @Column
    private String name;
    @Column
    private String category;
    @Column
    private LocalDateTime date;
    @Column
    private String description;
    @Column
    @JsonIgnore
    private String email;
    @Column
    @ElementCollection
    private List<String> ingredients;
    @Column
    @ElementCollection
    private List<String> directions;

    public Recipe(RecipeDTO recipeDTO, String email) {
        this.description = recipeDTO.getDescription();
        this.directions = recipeDTO.getDirections();
        this.name = recipeDTO.getName();
        this.ingredients = recipeDTO.getIngredients();
        this.id = count++;
        this.category = recipeDTO.getCategory();
        this.date = LocalDateTime.now();
        this.email = email;
    }
}
