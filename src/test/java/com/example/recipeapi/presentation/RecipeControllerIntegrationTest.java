package com.example.recipeapi.presentation;

import com.example.recipeapi.presentation.dto.RecipeDto;
import com.example.recipeapi.presentation.mapper.RecipeMapper;
import com.example.recipeapi.repository.RecipeRepository;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.example.recipeapi.dummies.FakeObjects.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.stream.StreamSupport;


@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerIntegrationTest {

    @Autowired
    private RecipeController recipeController;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenRecipeControllerInjectedThenNotNull() {
        assertThat(recipeController).isNotNull();
    }

    @Test
    void getAllRecipes() throws Exception {
        /***
         * Prepare scenario: fetch csv file for validation of the record size
         * Perform operation
         * Assert result
         */

        var path = Paths.get(ClassLoader.getSystemResource("data_recipe.csv").toURI());

        // with - 1 we skip the headers
        var numRecipes = new CSVReader(new FileReader(path.toString()))
                .readAll().size() - 1;

        this.mockMvc.perform(get("/recipe"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(numRecipes)));
    }

    @Test
    void findByIdShouldReturnARecipe() throws Exception {
        /***
         * Prepare scenario: first fetch an existing recipe then use the id
         * Perform operation
         * Assert result
         */

        var recipe = StreamSupport.stream(this.recipeRepository.findAll().spliterator(), false).toList().get(0);

        this.mockMvc.perform(get("/recipe/{id}", recipe.getId()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipe.getId()))
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.ingredients").value(recipe.getIngredients()))
                .andExpect(jsonPath("$.instructions").value(recipe.getInstructions()))
                .andExpect(jsonPath("$.persons").value(recipe.getPersons()))
                .andExpect(jsonPath("$.vegetarian").value(recipe.getVegetarian()));
    }

    @Test
    void createAndReturn() throws Exception {
        /***
         * Prepare scenario: generate fake recipe for post request
         * Perform operation
         * Assert result
         */

        var recipe = RecipeMapper.INSTANCE.recipeToRecipeDto(fakeRecipes().get(0));
        var requestJson = new Gson().toJson(recipe);

        this.mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.ingredients").value(recipe.getIngredients()))
                .andExpect(jsonPath("$.instructions").value(recipe.getInstructions()))
                .andExpect(jsonPath("$.persons").value(recipe.getPersons()))
                .andExpect(jsonPath("$.vegetarian").value(recipe.getVegetarian()));
    }

    @Test
    void updateAndReturn() throws Exception {
        /***
         * Prepare scenario: first post an existing recipe then modify the title for update func
         * Perform operation
         * Assert result
         */

        var gson = new Gson();
        var recipe = RecipeMapper.INSTANCE.recipeToRecipeDto(fakeRecipes().get(0));
        var requestJson = gson.toJson(recipe);

        var response = this.mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var recipeDto = gson.fromJson(response, RecipeDto.class);
        recipeDto.setTitle("A test");

        var requestJsonUpdate = gson.toJson(recipeDto);

        this.mockMvc.perform(put("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJsonUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("A test"))
                .andExpect(jsonPath("$.ingredients").isNotEmpty())
                .andExpect(jsonPath("$.instructions").isNotEmpty())
                .andExpect(jsonPath("$.persons").value(recipe.getPersons()))
                .andExpect(jsonPath("$.vegetarian").value(recipe.getVegetarian()));
    }

    @Test
    void deleteById() throws Exception {
        /***
         * Prepare scenario: first post a new recipe, delete the recipe and apply deletion
         * Perform operation
         * Assert result
         */

        var gson = new Gson();
        var recipe = RecipeMapper.INSTANCE.recipeToRecipeDto(fakeRecipes().get(0));
        var requestJson = gson.toJson(recipe);

        var postResponse = this.mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var recipeDto = gson.fromJson(postResponse, RecipeDto.class);

        this.mockMvc.perform(delete("/recipe/{id}", recipeDto.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        var responseCheckExistence = this.mockMvc.perform(get("/recipe/{id}", recipeDto.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var actual = gson.fromJson(responseCheckExistence, Object.class);

        assertThat(actual).isNull();
    }
}