package com.example.recipeapi.repository;

import com.example.recipeapi.document.Favorite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface FavoriteRepository extends ElasticsearchRepository<Favorite, String> {
}
