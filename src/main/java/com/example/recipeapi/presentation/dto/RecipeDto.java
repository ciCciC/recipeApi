package com.example.recipeapi.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private String id;
    private String title;
    private String cleanedIngredients;
    private String instructions;
    private String persons;
    private String vegetarian;
    private String imageName;
}
