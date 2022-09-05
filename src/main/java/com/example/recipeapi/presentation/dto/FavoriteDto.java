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
public class FavoriteDto {
    private String id;
    @NotBlank(message = "Is mandatory")
    private String userId;
    @NotBlank(message = "Is mandatory")
    private RecipeDto recipeDto;
}



