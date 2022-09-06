package com.example.recipeapi.business;

import com.example.recipeapi.business.entity.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import static com.example.recipeapi.dummies.FakeObjects.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private ElasticsearchRestTemplate es;

    @InjectMocks
    private SearchService searchService;

    @Test
    void searchAllVeggies() {
        var query = fakeSearchAllVeggies();
        var docType = Recipe.class;
        var expected = fakeSearchHits();

        when(es.search(isA(NativeSearchQuery.class), eq(docType))).thenReturn(expected);

        var actual = this.searchService.filter(query, docType).get();

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(expected.stream().count());

        verify(es).search(isA(NativeSearchQuery.class), eq(docType));
    }

    @Test
    void searchNpersonsHavePotatoes() {
        var query = fakeSearchNpersonsHavePotatoes(4);
        var docType = Recipe.class;
        var expected = fakeSearchHits();

        when(es.search(isA(NativeSearchQuery.class), eq(docType))).thenReturn(expected);

        var actual = this.searchService.filter(query, docType).get();

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(expected.stream().count());

        verify(es).search(isA(NativeSearchQuery.class), eq(docType));
    }

    @Test
    void searchWithoutSalmonAsIngredientsAndHasOvenAsInstructions() {
        var query = fakeSearchWithoutSalmonAsIngredientsAndHasOvenAsInstructions();
        var docType = Recipe.class;
        var expected = fakeSearchHits();

        when(es.search(isA(NativeSearchQuery.class), eq(docType))).thenReturn(expected);

        var actual = this.searchService.filter(query, docType).get();

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(expected.stream().count());

        verify(es).search(isA(NativeSearchQuery.class), eq(docType));
    }
}