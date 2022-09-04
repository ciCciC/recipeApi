package com.example.recipeapi.repository;

import com.example.recipeapi.document.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, String> {
}
