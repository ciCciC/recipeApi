package com.example.recipeapi.presentation;

import com.example.recipeapi.business.DataLoaderService;
import com.example.recipeapi.business.RecipeService;
import com.example.recipeapi.business.SearchService;
import com.example.recipeapi.document.Recipe;
import com.example.recipeapi.presentation.dto.PageDto;
import com.example.recipeapi.presentation.dto.RecipeDto;
import com.example.recipeapi.presentation.mapper.RecipeMapper;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("recipe")
public class RecipeController extends BaseController<RecipeDto> {

    private final RecipeService recipeService;
    private final DataLoaderService dataLoaderService;
    private final SearchService searchService;

    public RecipeController(RecipeService recipeService, DataLoaderService dataLoaderService, SearchService searchService) throws URISyntaxException, IOException, CsvException {
        this.recipeService = recipeService;
        this.dataLoaderService = dataLoaderService;
        this.searchService = searchService;
        this.dataLoaderService.removeAll();
        this.dataLoaderService.populate();
    }

    @GetMapping()
    @ResponseBody
    public Optional<Iterable<Recipe>> getAll(){
        return this.recipeService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Optional<RecipeDto> findById(@PathVariable String id) throws Exception {
        Optional<Recipe> recipe = this.recipeService.findById(id);
        return recipe.map(RecipeMapper.INSTANCE::recipeToRecipeDto);
    }

    @PostMapping()
    @ResponseBody
    public RecipeDto createRecipe(@RequestBody RecipeDto recipeDto){
        var created = this.recipeService.create(RecipeMapper.INSTANCE.recipeDtoToRecipe(recipeDto));
        return RecipeMapper.INSTANCE.recipeToRecipeDto(created);
    }

    @PutMapping()
    @ResponseBody
    public RecipeDto updateRecipe(@RequestBody RecipeDto recipeDto) throws Exception {
        var updated = this.recipeService.update(RecipeMapper.INSTANCE.recipeDtoToRecipe(recipeDto));
        return RecipeMapper.INSTANCE.recipeToRecipeDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteRecipe(@PathVariable String id) {
        this.recipeService.deleteById(id);
    }

    /***
     * This is optional
     * @throws IOException
     * @throws URISyntaxException
     * @throws CsvException
     */
    @GetMapping("/populate")
    public void populate() throws IOException, URISyntaxException, CsvException {
        this.dataLoaderService.populate();
    }

    @Override
    public Optional<PageDto<RecipeDto>> queryPage(int page) {
        return Optional.empty();
    }

    @Override
    public Optional<PageDto<RecipeDto>> searchBooksByTitle(String q, int page) {
        return Optional.empty();
    }

    @Override
    public Optional<PageDto<RecipeDto>> searchBooksByAmountPages(String q, int page) {
        return Optional.empty();
    }

}
