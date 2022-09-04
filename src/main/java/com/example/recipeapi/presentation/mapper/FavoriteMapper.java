package com.example.recipeapi.presentation.mapper;

import com.example.recipeapi.document.Favorite;
import com.example.recipeapi.presentation.dto.FavoriteDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface FavoriteMapper {

    FavoriteMapper INSTANCE = Mappers.getMapper(FavoriteMapper.class);
    FavoriteDto favoriteToFavoriteDto(Favorite favorite);
    Favorite favoriteDtoToFavorite(FavoriteDto favoriteDto);

    default List<FavoriteDto> toFavoriteDTOs(List<Favorite> favorites) {
        return favorites.stream().map(FavoriteMapper.INSTANCE::favoriteToFavoriteDto).collect(Collectors.toList());
    }
}
