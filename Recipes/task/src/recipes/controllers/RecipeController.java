package recipes.controllers;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.models.Recipe;
import recipes.models.RecipeResponse;
import recipes.models.dto.RecipeDTO;
import recipes.models.dto.UserDTO;
import recipes.services.impl.RecipeServiceImpl;
import recipes.services.impl.UserDetailsServiceImpl;

import javax.validation.Valid;

@RestController
public class RecipeController {

    private final RecipeServiceImpl recipeService;


    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    public RecipeController(RecipeServiceImpl recipeService, UserDetailsServiceImpl userDetailsService) {
        this.recipeService = recipeService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> postRecipe(@Valid @RequestBody RecipeDTO recipeDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();
        RecipeResponse response = recipeService.addRecipe(recipeDTO, details.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO){
        if(userDetailsService.isExists(userDTO)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String encodedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        userDetailsService.addUser(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> putRecipe(@Valid @RequestBody RecipeDTO recipeDTO, @PathVariable @NonNull Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();
        if(!recipeService.isExist(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!recipeService.isAuthor(id, details.getUsername())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        recipeService.putRecipe(recipeDTO, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable Long id){
        Recipe recipe = recipeService.getRecipe(id);
        if(recipe == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> searchRecipe(@RequestParam(required = false)  String name, @RequestParam(required = false) String category){
        if(name != null && category != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if(name != null){
            if(name.trim().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(recipeService.searchByName(name), HttpStatus.OK);
        }else if(category != null){
            if(category.trim().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(recipeService.searchByCategory(category), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();
        if(!recipeService.isExist(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!recipeService.isAuthor(id, details.getUsername())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        recipeService.deleteRecipe(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
