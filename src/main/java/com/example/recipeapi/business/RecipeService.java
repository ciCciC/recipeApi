package com.example.recipeapi.business;

import com.example.recipeapi.business.entity.Recipe;
import com.example.recipeapi.repository.FavoriteRepository;
import com.example.recipeapi.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final FavoriteRepository favoriteRepository;

    public RecipeService(
            RecipeRepository recipeRepository,
            FavoriteRepository favoriteRepository){
        this.recipeRepository = recipeRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Optional<List<Recipe>> findAll() {
        var recipes = this.recipeRepository.findAll();
        var mappedToList = StreamSupport.stream(recipes.spliterator(), false).toList();
        return Optional.of(mappedToList);
    }

    public Recipe create(Recipe recipe){
        return this.recipeRepository.save(recipe);
    }

    public Optional<Recipe> findById(String id) {
        return this.recipeRepository.findById(id);
    }

    public Recipe update(Recipe recipe) throws Exception {
        var updateRecipe = this.findById(recipe.getId())
                .orElseThrow(Exception::new);

        updateRecipe.setTitle(recipe.getTitle());
        updateRecipe.setIngredients(recipe.getIngredients());
        updateRecipe.setInstructions(recipe.getInstructions());
        updateRecipe.setPersons(recipe.getPersons());
        updateRecipe.setVegetarian(recipe.getVegetarian());

        return this.recipeRepository.save(updateRecipe);
    }

    public void deleteById(String id){
        this.recipeRepository.deleteById(id);
        this.favoriteRepository.deleteFavoritesByRecipeId(id);
    }

}
