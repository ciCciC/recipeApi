package com.example.recipeapi.business;

import com.example.recipeapi.document.Recipe;
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

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
public class DataLoaderService {

    private final ElasticsearchRestTemplate es;

    public DataLoaderService(@Qualifier("elsTemp") ElasticsearchRestTemplate es){
        this.es = es;
    }

    public void removeAll(){
        var allRecords = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .build();

        this.es.delete(allRecords, Recipe.class);
    }

    private IndexQuery mapToQuery(Recipe recipe, Gson gson) {
        var indexQuery = new IndexQuery();
        indexQuery.setSource(gson.toJson(recipe));
        return indexQuery;
    }

    private Recipe buildFromStringArr(String[] raw){
        return Recipe.builder()
                .title(raw[1])
                .cleanedIngredients(raw[2])
                .instructions(raw[3])
                .imageName("")
                .persons(raw[5])
                .vegetarian(raw[6])
                .build();
    }

    public void populate() throws URISyntaxException, IOException, CsvException {
        var path = Paths.get(ClassLoader.getSystemResource("semi_data_recipe.csv").toURI());
        var recipes = new CSVReader(new FileReader(path.toString())).readAll();

        var indexQueryList = recipes.stream()
                .skip(1)
                .parallel()
                .map(this::buildFromStringArr)
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

}
