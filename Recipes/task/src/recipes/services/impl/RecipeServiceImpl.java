package recipes.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.models.Recipe;
import recipes.models.RecipeResponse;
import recipes.models.dto.RecipeDTO;
import recipes.repositories.RecipeRepository;
import recipes.services.RecipeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeResponse addRecipe(RecipeDTO recipeDTO, String email) {
        Recipe recipe = new Recipe(recipeDTO, email);
        recipeRepository.save(recipe);
        return new RecipeResponse(recipe);
    }

    @Override
    public Recipe getRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        return recipeOptional.orElse(null);
    }

    public void deleteAll(){
        recipeRepository.deleteAll();
    }
    public boolean isExist(Long id) {
        return recipeRepository.findById(id).isPresent();
    }
    @Override
    public boolean isAuthor(Long id, String email) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if(recipeOptional.isPresent() && recipeOptional.get().getEmail().equals(email)){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if(recipeOptional.isPresent()){
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public boolean putRecipe(RecipeDTO recipeDTO, Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if(recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            recipe.setDate(LocalDateTime.now());
            recipe.setIngredients(recipeDTO.getIngredients());
            recipe.setCategory(recipeDTO.getCategory());
            recipe.setName(recipeDTO.getName());
            recipe.setDirections(recipeDTO.getDirections());
            recipe.setDescription(recipeDTO.getDescription());
            recipeRepository.deleteById(id);
            recipeRepository.save(recipe);
            return true;
        }
        return false;
    }


    @Override
    public List<Recipe> searchByName(String name){
        return recipeRepository.searchByName('%' + name + '%');
    }
    @Override
    public List<Recipe> searchByCategory(String category){
        return recipeRepository.searchByCategory(category);
    }
}
