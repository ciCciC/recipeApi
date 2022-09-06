package com.example.recipeapi.presentation;

import com.example.recipeapi.presentation.dto.CriteriaDto;
import com.example.recipeapi.presentation.dto.RecipeDto;
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
import java.util.Locale;

import static com.example.recipeapi.dummies.FakeObjects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerIntegrationTest {

    @Autowired
    private SearchController searchController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenSearchControllerInjectedThenNotNull() {
        assertThat(searchController).isNotNull();
    }

    @Test
    public void searchAllVeggies() throws Exception {
        /***
         * Prepare scenario: generate "fetch all veggies" search criteria then apply the query and assert on size and that all are in fact veggie
         * Perform operation
         * Assert result
         */

        var criteriaDtos = fakeSearchAllVeggies();
        var recipeDtos = applySearchAndGetResult(criteriaDtos);

        assertThat(recipeDtos.size()).isGreaterThan(1);
        assertThat(recipeDtos.stream().allMatch(f -> Integer.parseInt(f.getVegetarian()) == 1)).isTrue();
    }

    @Test
    public void searchNpersonsHavePotatoes() throws Exception {
        /***
         * Prepare scenario: generate "search recipes for N persons along with potatoes" search criteria then apply the query and assert on n persons and potatoes exists in ingredients
         * Perform operation
         * Assert result
         */

        int numberOfPersons = 4;
        var criteriaDtos = fakeSearchNpersonsHavePotatoes(numberOfPersons);
        var recipeDtos = applySearchAndGetResult(criteriaDtos);

        assertThat(recipeDtos.size()).isGreaterThan(1);
        assertThat(recipeDtos.stream().allMatch(f -> Integer.parseInt(f.getPersons()) == numberOfPersons)).isTrue();
        assertThat(recipeDtos.stream().allMatch(f -> f.getIngredients().toLowerCase(Locale.ROOT).contains("potatoes"))).isTrue();
    }

    @Test
    public void searchWithoutSalmonAsIngredientsAndHasOvenAsInstructions() throws Exception {
        /***
         * Prepare scenario: generate "search recipes that don't have salmon in ingredients but have potatoes in instructions" search criteria then apply the query and assert on presence of oven in instructions and absence of salmon in ingredients
         * Perform operation
         * Assert result
         */

        var criteriaDtos = fakeSearchWithoutSalmonAsIngredientsAndHasOvenAsInstructions();
        var recipeDtos = applySearchAndGetResult(criteriaDtos);

        assertThat(recipeDtos.size()).isGreaterThan(1);
        assertThat(recipeDtos.stream().allMatch(f -> f.getInstructions().toLowerCase(Locale.ROOT).contains("oven"))).isTrue();
        assertThat(recipeDtos.stream().noneMatch(f -> f.getIngredients().toLowerCase(Locale.ROOT).contains("salmon"))).isTrue();
    }

    private ArrayList<RecipeDto> applySearchAndGetResult(List<CriteriaDto> criteriaDtos) throws Exception {
        var requestJson = new Gson().toJson(criteriaDtos);

        var result = this.mockMvc.perform(get("/search/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var contentJson = result.getResponse().getContentAsString();

        return new Gson().fromJson(contentJson, new TypeToken<List<RecipeDto>>(){}.getType());
    }

}