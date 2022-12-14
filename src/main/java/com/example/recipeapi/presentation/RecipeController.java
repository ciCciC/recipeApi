package com.example.recipeapi.presentation;

import com.example.recipeapi.business.RecipeService;
import com.example.recipeapi.presentation.dto.RecipeDto;
import com.example.recipeapi.presentation.mapper.RecipeMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("recipe")
public class RecipeController extends BaseController<RecipeDto> {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @ResponseBody
    @Override
    public Optional<List<RecipeDto>> getAll() {
        var recipes = this.recipeService.findAll();
        return recipes.map(RecipeMapper.INSTANCE::toRecipeDTOs);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Optional<RecipeDto> findById(@PathVariable String id) throws Exception {
        var recipe = this.recipeService.findById(id);
        return recipe.map(RecipeMapper.INSTANCE::recipeToRecipeDto);
    }

    @PostMapping
    @ResponseBody
    @Override
    public RecipeDto create(@Valid @RequestBody RecipeDto recipeDto) {
        var recipe = this.recipeService.create(RecipeMapper.INSTANCE.recipeDtoToRecipe(recipeDto));
        return RecipeMapper.INSTANCE.recipeToRecipeDto(recipe);
    }

    @PutMapping
    @ResponseBody
    @Override
    public RecipeDto update(@Valid @RequestBody RecipeDto recipeDto) throws Exception {
        var recipe = this.recipeService.update(RecipeMapper.INSTANCE.recipeDtoToRecipe(recipeDto));
        return RecipeMapper.INSTANCE.recipeToRecipeDto(recipe);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public void deleteById(@PathVariable String id) {
        this.recipeService.deleteById(id);
    }

}
