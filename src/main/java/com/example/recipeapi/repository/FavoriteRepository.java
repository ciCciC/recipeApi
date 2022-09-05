package com.example.recipeapi.repository;

import com.example.recipeapi.business.entity.Favorite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;


public interface FavoriteRepository extends ElasticsearchRepository<Favorite, String> {
    Optional<List<Favorite>> findByUserId(String value);
    void deleteFavoritesByRecipeId(String id);
    void deleteFavoritesByUserId(String id);
}
