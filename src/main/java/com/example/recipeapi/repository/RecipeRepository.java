package com.example.recipeapi.repository;

import com.example.recipeapi.business.entity.Recipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecipeRepository extends ElasticsearchRepository<Recipe, String> {
}
