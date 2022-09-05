package com.example.recipeapi.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(indexName = "recipe")
public class Recipe {

    @Id
    private String id;
    private String title;
    private String ingredients;
    private String instructions;
    private String persons;
    private String vegetarian;
}