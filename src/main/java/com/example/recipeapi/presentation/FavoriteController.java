package com.example.recipeapi.presentation;

import com.example.recipeapi.business.DataLoaderService;
import com.example.recipeapi.business.FavoriteService;
import com.example.recipeapi.document.Favorite;
import com.example.recipeapi.presentation.dto.FavoriteDto;
import com.example.recipeapi.presentation.mapper.FavoriteMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("favorite")
public class FavoriteController extends BaseController<FavoriteDto> {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService, DataLoaderService dataLoaderService){
        this.favoriteService = favoriteService;
        dataLoaderService.removeAll(Favorite.class);
    }

    @GetMapping
    @ResponseBody
    @Override
    public Optional<List<FavoriteDto>> getAll(@RequestParam String userId) {
        return this.favoriteService.findAll(userId);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Optional<FavoriteDto> findById(@PathVariable String id) {
        return this.favoriteService.findById(id);
    }

    @PostMapping
    @ResponseBody
    @Override
    public FavoriteDto create(@RequestBody FavoriteDto favoriteDto) {
        var favorite = this.favoriteService.create(FavoriteMapper.INSTANCE.favoriteDtoToFavorite(favoriteDto));
        return FavoriteMapper.INSTANCE.favoriteToFavoriteDto(favorite, favoriteDto.getRecipeDto());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public void deleteById(@PathVariable String id){
        this.favoriteService.deleteById(id);
    }

    @GetMapping("/dummy/{id}")
    @ResponseBody
    public Favorite createDummy(@PathVariable String id){
        // TODO: Remove this func
        return this.favoriteService.create(Favorite.builder().userId("1").recipeId(id).build());
    }

//    @Override
//    Optional<PageDto<FavoriteDto>> queryPage(int page) {
//        return Optional.empty();
//    }
//
//    @Override
//    Optional<PageDto<FavoriteDto>> searchBooksByTitle(String q, int page) {
//        return Optional.empty();
//    }
//
//    @Override
//    Optional<PageDto<FavoriteDto>> searchBooksByAmountPages(String q, int page) {
//        return Optional.empty();
//    }
}
