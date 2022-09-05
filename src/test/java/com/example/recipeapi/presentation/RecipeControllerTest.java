package com.example.recipeapi.presentation;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private RecipeController recipeController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenRecipeControllerInjected_thenNotNull() {
        assertThat(recipeController).isNotNull();
    }

    @Test
    void getAll() throws Exception {
        var path = Paths.get(ClassLoader.getSystemResource("data_recipe.csv").toURI());

        // with - 1 we skip the headers
        var numRecipes = new CSVReader(new FileReader(path.toString()))
                .readAll().size() - 1;

        this.mockMvc.perform(get("/recipe"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(numRecipes)));
    }

//    @Test
//    void findById() {
//    }
//
//    @Test
//    void create() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void deleteById() {
//    }
}