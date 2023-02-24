package recipes.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    Long id;

    public RecipeResponse(Recipe recipe) {
        this.id = recipe.getId();
    }
}
