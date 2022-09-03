package com.example.recipeapi.business;


import com.example.recipeapi.document.Recipe;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.IndexBoost;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
public class SearchService {

    private final ElasticsearchRestTemplate es;

    public SearchService(@Qualifier("elsTemp") ElasticsearchRestTemplate es){
        this.es = es;
    }

    public String genNewId(){
        var indexToHit = new IndexBoost("recipe", 2);
        var allIds = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .build();

        var x = this.es.search(allIds, Recipe.class);
        var id = x.getSearchHits().stream().map(SearchHit::getId);

//        var x = (Recipe) allIds.toArray()[allIds.size()-1];
        return id.toList().get(0);
    }


}
