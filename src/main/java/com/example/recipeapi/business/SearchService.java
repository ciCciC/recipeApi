package com.example.recipeapi.business;

import com.example.recipeapi.presentation.dto.CriteriaDto;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchService {

    private final ElasticsearchRestTemplate es;

    public SearchService(@Qualifier("elsTemp") ElasticsearchRestTemplate es){
        this.es = es;
    }

    public <R> Optional<List<R>> filter(List<CriteriaDto> criteriaDtoList, Class<R> docType){
        var boolQuery = QueryBuilders.boolQuery();

        criteriaDtoList.forEach(criteriaDto -> {
            var matchPhraseQuery = QueryBuilders.matchPhraseQuery(criteriaDto.getFieldName(), criteriaDto.getValue());
            if (criteriaDto.isCondition()){
                boolQuery.must(matchPhraseQuery);
            }else{
                boolQuery.mustNot(matchPhraseQuery);
            }
        });

        var searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();

        var result = this.es.search(searchQuery, docType)
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        return Optional.of(result);
    }
}
