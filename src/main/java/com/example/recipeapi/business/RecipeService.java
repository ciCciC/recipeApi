package com.example.recipeapi.business;

import com.example.recipeapi.document.Recipe;
import com.example.recipeapi.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecipeService {

    private final ElasticsearchRestTemplate es;

    private final RecipeRepository recipeRepository;

    public RecipeService(
            RecipeRepository recipeRepository,
            @Qualifier("elsTemp") ElasticsearchRestTemplate es){

        this.recipeRepository = recipeRepository;
        this.es = es;
    }

    public Optional<List<Recipe>> findAll() {
        var recipes = this.recipeRepository.findAll();
        var mappedToList = StreamSupport.stream(recipes.spliterator(), false).collect(Collectors.toList());
        return Optional.of(mappedToList);
    }

    public Recipe create(Recipe recipe){
        return this.recipeRepository.save(recipe);
    }

    public Optional<Recipe> findById(String id) throws Exception {
        return Optional.ofNullable(this.recipeRepository.findById(id).orElseThrow(Exception::new));
    }

    public Recipe update(Recipe recipe) throws Exception {
        var updateRecipe = this.findById(recipe.getId())
                .orElseThrow(Exception::new);

        updateRecipe.setTitle(recipe.getTitle());
        updateRecipe.setCleanedIngredients(recipe.getCleanedIngredients());
        updateRecipe.setInstructions(recipe.getInstructions());
        updateRecipe.setPersons(recipe.getPersons());
        updateRecipe.setVegetarian(recipe.getVegetarian());

        return this.recipeRepository.save(updateRecipe);
    }

    public void deleteById(String id){
        //TODO: also delete from Favorite index!
        this.recipeRepository.deleteById(id);
    }

}
