package com.example.recipeapi.presentation;

import com.example.recipeapi.business.FavoriteService;
import com.example.recipeapi.presentation.dto.FavoriteDto;
import com.example.recipeapi.presentation.mapper.FavoriteMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("favorite")
public class FavoriteController extends BaseController<FavoriteDto> {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService){
        this.favoriteService = favoriteService;
    }

    @GetMapping
    @ResponseBody
    @Override
    public Optional<List<FavoriteDto>> getAll(){
        return this.favoriteService.findAll();
    }

    @GetMapping("/user")
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
    public FavoriteDto create(@Valid @RequestBody FavoriteDto favoriteDto) {
        var favorite = this.favoriteService.create(FavoriteMapper.INSTANCE.favoriteDtoToFavorite(favoriteDto));
        return FavoriteMapper.INSTANCE.favoriteToFavoriteDto(favorite, favoriteDto.getRecipeDto());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public void deleteById(@PathVariable String id){
        this.favoriteService.deleteById(id);
    }
}
