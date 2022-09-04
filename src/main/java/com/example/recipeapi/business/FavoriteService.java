package com.example.recipeapi.business;

import com.example.recipeapi.document.Favorite;
import com.example.recipeapi.repository.FavoriteRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FavoriteService {

    private final ElasticsearchRestTemplate es;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           @Qualifier("elsTemp") ElasticsearchRestTemplate es){
        this.favoriteRepository = favoriteRepository;
        this.es = es;
    }

    public Optional<List<Favorite>> findAll(String userId) {
        // TODO: try using within the repository eg annotations
        // TODO: include full info of recipes within the response
        var q = QueryBuilders.boolQuery()
                .should(QueryBuilders
                        .queryStringQuery(userId)
                        .field("userId"));

        var sq = new NativeSearchQueryBuilder()
                .withQuery(q)
                .build();

        var result = this.es.search(sq, Favorite.class)
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        return Optional.of(result);
    }

    public Favorite create(Favorite favorite){
        return this.favoriteRepository.save(favorite);
    }

    public Optional<Favorite> findById(String id) throws Exception {
        return Optional.ofNullable(this.favoriteRepository.findById(id).orElseThrow(Exception::new));
    }

    public void deleteById(String id){
        this.favoriteRepository.deleteById(id);
    }


}
