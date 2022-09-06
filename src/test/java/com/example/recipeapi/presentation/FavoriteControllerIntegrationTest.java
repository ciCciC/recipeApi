package com.example.recipeapi.presentation;

import com.example.recipeapi.presentation.dto.FavoriteDto;
import com.example.recipeapi.presentation.mapper.RecipeMapper;
import com.example.recipeapi.repository.FavoriteRepository;
import com.example.recipeapi.repository.RecipeRepository;
import com.example.recipeapi.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerIntegrationTest {

    @Autowired
    private FavoriteController favoriteController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFavoriteControllerInjectedThenNotNull() {
        assertThat(favoriteController).isNotNull();
    }

    @Test
    void getAllFavoritesOfAuser() throws Exception {
        /***
         * Prepare scenario: get all existing favorites then choose first elements' user to use its userId
         * Perform operation
         * Assert result
         */

        var gson = new Gson();

        var favoritesJson = this.mockMvc.perform(get("/favorite")).andReturn().getResponse().getContentAsString();
        ArrayList<FavoriteDto> favorites = gson.fromJson(favoritesJson, new TypeToken<List<FavoriteDto>>(){}.getType());

        var userId = favorites.get(0).getUserId();

        this.mockMvc.perform(get("/favorite/user").param("userId", userId))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThan(1)));
    }

    @Test
    void findById() throws Exception {
        /***
         * Prepare scenario: get an existing favorite then use the id
         * Perform operation
         * Assert result
         */

        var gson = new Gson();

        var favoritesJson = this.mockMvc.perform(get("/favorite")).andReturn().getResponse().getContentAsString();
        ArrayList<FavoriteDto> favorites = gson.fromJson(favoritesJson, new TypeToken<List<FavoriteDto>>(){}.getType());

        var favoriteDto = favorites.get(0);

        this.mockMvc.perform(get("/favorite/{id}", favoriteDto.getId()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(favoriteDto.getId()))
                .andExpect(jsonPath("$.userId").value(favoriteDto.getUserId()))
                .andExpect(jsonPath("$.recipeDto.id").value(favoriteDto.getRecipeDto().getId()));
    }

    @Test
    void create() throws Exception {
        /***
         * Prepare scenario: fetch existing recipe and user to create a favorite element
         * Perform operation
         * Assert result
         */
        var recipe = StreamSupport.stream(this.recipeRepository.findAll().spliterator(), false).toList().get(0);
        var recipeDto = RecipeMapper.INSTANCE.recipeToRecipeDto(recipe);
        var user = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).toList().get(0);

        var favoriteDto = FavoriteDto.builder()
                .userId(user.getId())
                .recipeDto(recipeDto)
                .build();

        var requestJson = new Gson().toJson(favoriteDto);

        this.mockMvc.perform(post("/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(favoriteDto.getUserId()))
                .andExpect(jsonPath("$.recipeDto.id").value(favoriteDto.getRecipeDto().getId()));
    }

    @Test
    void deleteById() throws Exception {
        /***
         * Prepare scenario: fetch existing favorite to be deleted
         * Perform operation
         * Assert result
         */

        var favorite = StreamSupport.stream(this.favoriteRepository.findAll().spliterator(), false).toList().get(0);

        this.mockMvc.perform(delete("/favorite/{id}", favorite.getId()))
                .andExpect(status().isOk());

        assertThat(this.favoriteRepository.findById(favorite.getId()).isEmpty()).isTrue();
    }
}