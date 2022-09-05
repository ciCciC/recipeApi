package com.example.recipeapi.presentation;

import com.example.recipeapi.business.SearchService;
import com.example.recipeapi.business.entity.Recipe;
import com.example.recipeapi.presentation.dto.CriteriaDto;
import com.example.recipeapi.presentation.dto.RecipeDto;
import com.example.recipeapi.presentation.mapper.RecipeMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }

    @GetMapping("/recipes")
    @ResponseBody
    public Optional<List<RecipeDto>> filterRecipes(@Valid @RequestBody List<CriteriaDto> criteriaDto) throws ClassNotFoundException {
        var recipes = this.searchService.filter(criteriaDto, Recipe.class);
        return recipes.map(RecipeMapper.INSTANCE::toRecipeDTOs);
    }
}
