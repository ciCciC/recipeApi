package com.example.recipeapi.presentation;

import com.example.recipeapi.repository.RecipeRepository;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void createRecipeAndReturn() throws Exception {
        var requestJson = "";

        this.mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

//    @Test
//    void update() {
//    }
//
//    @Test
//    void deleteById() {
//    }
}