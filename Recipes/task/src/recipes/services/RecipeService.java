package recipes.services;

import recipes.models.Recipe;
import recipes.models.RecipeResponse;
import recipes.models.dto.RecipeDTO;

import java.util.List;

public interface RecipeService {
    public RecipeResponse addRecipe(RecipeDTO recipeDTO, String email);
    public Recipe getRecipe(Long id);

    public boolean isAuthor(Long id, String email);

    boolean deleteRecipe(Long id);

    boolean putRecipe(RecipeDTO recipeDTO, Long id);

    List<Recipe> searchByName(String name);

    List<Recipe> searchByCategory(String category);
}
