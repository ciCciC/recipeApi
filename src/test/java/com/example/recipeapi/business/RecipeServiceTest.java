package com.example.recipeapi.business;


import com.example.recipeapi.repository.FavoriteRepository;
import com.example.recipeapi.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static com.example.recipeapi.dummies.FakeObjects.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void findAllRecipes() {
        var expected = fakeRecipes();
        when(recipeRepository.findAll()).thenReturn(expected);
        var actual = this.recipeService.findAll();

        assertThat(actual.get().size()).isEqualTo(expected.size());
        verify(recipeRepository).findAll();
    }

    @Test
    void createRecipeAndReturn() {
        var expected = fakeRecipes().get(0);

        when(recipeRepository.save(eq(expected))).thenReturn(expected);

        var actual = this.recipeService.create(expected);

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(recipeRepository).save(eq(actual));
    }

    @Test
    void findById() {
        var expected = fakeRecipes().get(0);
        var fakeId = "someFakeId";
        expected.setId(fakeId);

        when(recipeRepository.findById(fakeId)).thenReturn(Optional.of(expected));

        var actual = this.recipeService.findById(fakeId).get();

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(recipeRepository).findById(fakeId);
    }

    @Test
    void update() throws Exception {
        var newTitle = "some fake title";
        var fakeId = "someFakeId";

        var expected = fakeRecipes().get(0);
        expected.setId(fakeId);
        expected.setTitle(newTitle);

        when(recipeRepository.findById(fakeId)).thenReturn(Optional.of(expected));
        when(recipeRepository.save(eq(expected))).thenReturn(expected);

        var actual = this.recipeService.update(expected);

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(actual.getTitle()).isEqualTo(newTitle);

        verify(recipeRepository).findById(fakeId);
        verify(recipeRepository).save(eq(actual));
    }

    @Test
    void deleteById() {
        var fakeId = "someFakeId";

        doNothing().when(recipeRepository).deleteById(fakeId);
        doNothing().when(favoriteRepository).deleteFavoritesByRecipeId(fakeId);

        this.recipeService.deleteById(fakeId);

        verify(recipeRepository).deleteById(fakeId);
        verify(favoriteRepository).deleteFavoritesByRecipeId(fakeId);
    }
}