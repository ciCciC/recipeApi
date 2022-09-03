package com.example.recipeapi.repository;

import com.example.recipeapi.document.Recipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecipeRepository extends ElasticsearchRepository<Recipe, String> {
}
