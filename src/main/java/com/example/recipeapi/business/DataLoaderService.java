package com.example.recipeapi.business;

import com.example.recipeapi.business.entity.Favorite;
import com.example.recipeapi.business.entity.Recipe;
import com.example.recipeapi.business.entity.User;
import com.example.recipeapi.repository.RecipeRepository;
import com.example.recipeapi.repository.UserRepository;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.query.BulkOptions;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
public class DataLoaderService {

    private final ElasticsearchRestTemplate es;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public DataLoaderService(UserRepository userRepository,
                             RecipeRepository recipeRepository,
            @Qualifier("elsTemp") ElasticsearchRestTemplate es) throws URISyntaxException, IOException, CsvException {
        this.es = es;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.removeAll(Recipe.class);
        this.removeAll(Favorite.class);
        this.removeAll(User.class);
        this.populateRecipes();
        this.populateUsers();
        this.genFakeCustomerBehavior();
    }

    public <T> void removeAll(Class<T> docType){
        var allRecords = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .build();

        this.es.delete(allRecords, docType);
    }

    private <T> IndexQuery mapToQuery(T document, Gson gson) {
        var indexQuery = new IndexQuery();
        indexQuery.setSource(gson.toJson(document));
        return indexQuery;
    }

    public void populateRecipes() throws URISyntaxException, IOException, CsvException {
        var path = Paths.get(ClassLoader.getSystemResource("data_recipe.csv").toURI());
        var recipes = new CSVReader(new FileReader(path.toString())).readAll();

        var indexQueryList = recipes.stream()
                .skip(1)
                .parallel()
                .map(raw -> Recipe.builder()
                        .title(raw[1])
                        .ingredients(raw[2])
                        .instructions(raw[3])
                        .persons(raw[5])
                        .vegetarian(raw[6])
                        .build())
                .map(recipe -> mapToQuery(recipe, new Gson()))
                .toList();

        es.bulkIndex(
                indexQueryList,
                BulkOptions
                        .builder()
                        .withRefreshPolicy(RefreshPolicy.IMMEDIATE)
                        .build(),
                Recipe.class);
    }

    public void populateUsers(){
        var indexQueryList = Stream.of("Mike Holloway", "Jack Sparrow", "Aaron Dasinki")
                .map(name -> name.split(" "))
                .map(name -> mapToQuery(User.builder()
                        .fname(name[0])
                        .lname(name[1])
                        .build(), new Gson())
                ).toList();

        es.bulkIndex(
                indexQueryList,
                BulkOptions
                        .builder()
                        .withRefreshPolicy(RefreshPolicy.IMMEDIATE)
                        .build(),
                User.class);
    }

    public void genFakeCustomerBehavior(){
        var users = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).toList();
        var recipes = StreamSupport.stream(this.recipeRepository.findAll().spliterator(), false).toList();

        var firstCandidate = users.get(0).getId();
        var secondCandidate = users.get(1).getId();

        var firstCandidateFavorites = recipes.stream()
                .limit(6)
                .map(Recipe::getId)
                .map(recipeId -> Favorite.builder().userId(firstCandidate).recipeId(recipeId).build())
                .toList();

        var secondCandidateFavorites = recipes.stream()
                .skip(10)
                .limit(2)
                .map(Recipe::getId)
                .map(recipeId -> Favorite.builder().userId(secondCandidate).recipeId(recipeId).build())
                .toList();

        var indexQueryList = Stream.concat(firstCandidateFavorites.stream(), secondCandidateFavorites.stream())
                .map(favorite -> mapToQuery(favorite, new Gson())).toList();

        es.bulkIndex(
                indexQueryList,
                BulkOptions
                        .builder()
                        .withRefreshPolicy(RefreshPolicy.IMMEDIATE)
                        .build(),
                Favorite.class);
    }

}
