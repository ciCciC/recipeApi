package com.example.recipeapi.presentation.mapper;

import com.example.recipeapi.document.Recipe;
import com.example.recipeapi.presentation.dto.RecipeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);
    RecipeDto recipeToRecipeDto(Recipe recipe);
    Recipe recipeDtoToRecipe(RecipeDto recipeDto);
}
