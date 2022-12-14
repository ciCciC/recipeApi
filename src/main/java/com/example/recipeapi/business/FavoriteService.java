package com.example.recipeapi.business;

import com.example.recipeapi.business.entity.Favorite;
import com.example.recipeapi.presentation.dto.FavoriteDto;
import com.example.recipeapi.presentation.mapper.FavoriteMapper;
import com.example.recipeapi.presentation.mapper.RecipeMapper;
import com.example.recipeapi.repository.FavoriteRepository;
import com.example.recipeapi.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           RecipeRepository recipeRepository){
        this.favoriteRepository = favoriteRepository;
        this.recipeRepository = recipeRepository;
    }

    public Optional<List<FavoriteDto>> findAll(String userId) {
        var qResult = this.favoriteRepository.findByUserId(userId);
        return qResult
                .map(favorites -> favorites.stream()
                        .map(favorite -> mapTo(favorite, favorite.getRecipeId()))
                                .toList()
                        );
    }

    public Optional<List<FavoriteDto>> findAll(){
        var favorites = StreamSupport.stream(this.favoriteRepository.findAll().spliterator(), false).toList();
        return Optional.of(favorites.stream().map(f -> mapTo(f, f.getRecipeId())).toList());
    }

    public Favorite create(Favorite favorite){
        return this.favoriteRepository.save(favorite);
    }

    public Optional<FavoriteDto> findById(String id) {
        return this.favoriteRepository.findById(id)
                .map(favorite -> mapTo(favorite, favorite.getRecipeId()));
    }

    private FavoriteDto mapTo(Favorite favorite, String recipeId){
        var recipe = this.recipeRepository.findById(recipeId)
                .map(RecipeMapper.INSTANCE::recipeToRecipeDto)
                .get();
        return FavoriteMapper.INSTANCE.favoriteToFavoriteDto(favorite, recipe);
    }

    public void deleteById(String id){
        this.favoriteRepository.deleteById(id);
    }

}
