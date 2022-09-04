package com.example.recipeapi.presentation;

import com.example.recipeapi.business.FavoriteService;
import com.example.recipeapi.presentation.dto.FavoriteDto;
import com.example.recipeapi.presentation.dto.PageDto;
import com.example.recipeapi.presentation.dto.RecipeDto;
import com.example.recipeapi.presentation.mapper.FavoriteMapper;
import com.example.recipeapi.presentation.mapper.RecipeMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("favorite")
public class FavoriteController extends BaseController<FavoriteDto> {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService){
        this.favoriteService = favoriteService;
    }

    @GetMapping("/{id}")
    @Override
    Optional<List<FavoriteDto>> getAll(@PathVariable String id) {
        var favorites = this.favoriteService.findAll(id);
        return favorites.map(FavoriteMapper.INSTANCE::toFavoriteDTOs);
    }

    @Override
    Optional<FavoriteDto> findById(@PathVariable String id) throws Exception {
        return Optional.empty();
    }

    @PostMapping()
    @ResponseBody
    public FavoriteDto create(@RequestBody FavoriteDto favoriteDto) {
        var created = this.favoriteService.create(FavoriteMapper.INSTANCE.favoriteDtoToFavorite(favoriteDto));
        return FavoriteMapper.INSTANCE.favoriteToFavoriteDto(created);
    }

    @Override
    Optional<PageDto<FavoriteDto>> queryPage(int page) {
        return Optional.empty();
    }

    @Override
    Optional<PageDto<FavoriteDto>> searchBooksByTitle(String q, int page) {
        return Optional.empty();
    }

    @Override
    Optional<PageDto<FavoriteDto>> searchBooksByAmountPages(String q, int page) {
        return Optional.empty();
    }
}
