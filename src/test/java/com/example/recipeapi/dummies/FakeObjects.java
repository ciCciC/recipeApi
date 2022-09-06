package com.example.recipeapi.dummies;

import com.example.recipeapi.business.entity.Favorite;
import com.example.recipeapi.business.entity.Recipe;
import com.example.recipeapi.business.entity.User;
import com.example.recipeapi.presentation.dto.CriteriaDto;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.TotalHitsRelation;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

import java.util.Arrays;
import java.util.List;

public class FakeObjects {
    public static List<Recipe> fakeRecipes(){
        return Arrays.asList(
                Recipe.builder()
                        .title("Roast Chicken With Acorn Squash Panzanella")
                        .ingredients("Chicken, Panzanella, Cucumber")
                        .instructions("Pat chicken dry with paper towels, season all over with 2 tsp. salt, and tie legs together with kitchen twine. Let sit at room temperature 1 hour.\\nMeanwhile, halve squash and scoop out seeds. Run a vegetable peeler along ridges of squash halves to remove skin. Cut each half into ½\\\"-thick wedges; arrange on a rimmed baking sheet.\\nCombine sage, rosemary, and 6 Tbsp. melted butter in a large bowl;")
                        .persons("2")
                        .vegetarian("0")
                        .build(),
                Recipe.builder()
                        .title("Spiced Lentil and Caramelized Onion Baked Eggsplant")
                        .ingredients("Lentil, onion, eggplant")
                        .instructions("Place an oven rack in the center of the oven, then preheat to 350°F.\\nIn a medium, oven-safe pan, heat 1 Tbsp. olive oil over medium heat. Add 1 large, thinly sliced onion and ½ tsp. Kosher salt. Cook, stirring often, until golden brown, about 25 minutes.\\nAdd ½ tsp. turmeric, 1 tsp. cumin, ¼ tsp. Aleppo pepper (or ⅛ tsp. crushed red pepper flakes), and 2 Tbsp. tomato paste. Cook and stir constantly until the onions are coated and the tomato paste has darkened slightly, about 2 minutes. Add ⅓ cup water; stir and scrape up all the browned bits on the bottom of the pan for 1 to 2 minutes, or until the liquid looks thickened and saucy. Add one 14-oz. can of lentil soup; cook, stirring to combine, 1 to 2 minutes.")
                        .persons("4")
                        .vegetarian("1")
                        .build());
    }

    public static List<Favorite> fakeFavorites(){
        return Arrays.asList(
                Favorite.builder().userId("1").recipeId("1").build(),
                Favorite.builder().userId("1").recipeId("2").build(),
                Favorite.builder().userId("1").recipeId("3").build(),
                Favorite.builder().userId("2").recipeId("6").build(),
                Favorite.builder().userId("2").recipeId("3").build()
        );
    }

    public static List<User> fakeUsers(){
        return Arrays.asList(
                User.builder().fname("Peter").lname("Jackson").build(),
                User.builder().fname("John").lname("Snow").build());
    }

    public static List<CriteriaDto> fakeSearchAllVeggies(){
        return Arrays.asList(
                CriteriaDto.builder().fieldName("vegetarian").value("1").condition(true).build()
        );
    }

    public static List<CriteriaDto> fakeSearchNpersonsHavePotatoes(int n){
        return Arrays.asList(
                CriteriaDto.builder().fieldName("persons").value(String.valueOf(n)).condition(true).build(),
                CriteriaDto.builder().fieldName("ingredients").value("potatoes").condition(true).build()
        );
    }

    public static List<CriteriaDto> fakeSearchWithoutSalmonAsIngredientsAndHasOvenAsInstructions(){
        return Arrays.asList(
                CriteriaDto.builder().fieldName("ingredients").value("salmon").condition(false).build(),
                CriteriaDto.builder().fieldName("instructions").value("oven").condition(true).build()
        );
    }

    public static SearchHits<Recipe> fakeSearchHits(){
        return new SearchHits<Recipe>() {
            @Override
            public AggregationsContainer<?> getAggregations() {
                return null;
            }

            @Override
            public float getMaxScore() {
                return 0;
            }

            @Override
            public SearchHit<Recipe> getSearchHit(int index) {
                return null;
            }

            @Override
            public List<SearchHit<Recipe>> getSearchHits() {
                return fakeRecipes().stream().map(f -> new SearchHit<Recipe>(null, null, null, 1f, null, null, f)).toList();
            }

            @Override
            public long getTotalHits() {
                return 0;
            }

            @Override
            public TotalHitsRelation getTotalHitsRelation() {
                return null;
            }

            @Override
            public Suggest getSuggest() {
                return null;
            }
        };
    }
}
