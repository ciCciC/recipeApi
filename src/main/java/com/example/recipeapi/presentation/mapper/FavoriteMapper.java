package com.example.recipeapi.presentation.mapper;

import com.example.recipeapi.business.entity.Favorite;
import com.example.recipeapi.presentation.dto.FavoriteDto;
import com.example.recipeapi.presentation.dto.RecipeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FavoriteMapper {

    FavoriteMapper INSTANCE = Mappers.getMapper(FavoriteMapper.class);

    default FavoriteDto favoriteToFavoriteDto(Favorite favorite, RecipeDto recipeDto){
        return FavoriteDto.builder()
                .id(favorite.getId())
                .userId(favorite.getUserId())
                .recipeDto(recipeDto)
                .build();
    }

    default Favorite favoriteDtoToFavorite(FavoriteDto favoriteDto){
        return Favorite.builder()
                .id(favoriteDto.getId())
                .userId(favoriteDto.getUserId())
                .recipeId(favoriteDto.getRecipeDto().getId())
                .build();
    }

}
