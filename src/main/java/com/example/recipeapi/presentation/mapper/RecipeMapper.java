package com.example.recipeapi.presentation.mapper;

import com.example.recipeapi.business.entity.Recipe;
import com.example.recipeapi.presentation.dto.RecipeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);
    RecipeDto recipeToRecipeDto(Recipe recipe);
    Recipe recipeDtoToRecipe(RecipeDto recipeDto);

    default List<RecipeDto> toRecipeDTOs(List<Recipe> recipes) {
        return recipes.stream().map(RecipeMapper.INSTANCE::recipeToRecipeDto).collect(Collectors.toList());
    }
}
