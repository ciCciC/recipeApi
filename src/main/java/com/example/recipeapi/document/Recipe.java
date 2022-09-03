package com.example.recipeapi.document;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@Data
@Document(indexName = "recipe")
public class Recipe {

    @Id
    private String id;
    private String title;
    private String cleanedIngredients;
    private String instructions;
    private String persons;
    private String vegetarian;
    private String imageName;

    @Builder
    public Recipe(String id, String title, String cleanedIngredients,
                  String instructions, String persons, String vegetarian,
                  String imageName) {
        this.id = id;
        this.title = title;
        this.cleanedIngredients = cleanedIngredients;
        this.instructions = instructions;
        this.persons = persons;
        this.vegetarian = vegetarian;
        this.imageName = imageName;
    }
}