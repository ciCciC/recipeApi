package com.example.recipeapi.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private String id;
    @NotBlank(message = "Is mandatory")
    private String title;
    @NotBlank(message = "Is mandatory")
    private String ingredients;
    @NotBlank(message = "Is mandatory")
    private String instructions;
    @NotBlank(message = "Is mandatory")
    private String persons;
    @NotBlank(message = "Is mandatory")
    private String vegetarian;
}
