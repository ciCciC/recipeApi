package com.example.recipeapi.business;


import com.example.recipeapi.presentation.dto.FavoriteDto;
import com.example.recipeapi.presentation.mapper.FavoriteMapper;
import com.example.recipeapi.repository.FavoriteRepository;
import com.example.recipeapi.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.example.recipeapi.dummies.FakeObjects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void findAllAndReturn() {
        var fakeUserId = "1";
        var expected = fakeFavorites().stream().filter(f -> f.getUserId().equals(fakeUserId)).toList();

        when(favoriteRepository.findByUserId(fakeUserId)).thenReturn(Optional.of(expected));
        when(recipeRepository.findById(isA(String.class))).thenReturn(Optional.of(fakeRecipes().get(0)));

        var actual = this.favoriteService.findAll(fakeUserId).get();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertLinesMatch(actual.stream().map(FavoriteDto::getUserId), Stream.of(fakeUserId, fakeUserId, fakeUserId));

        verify(favoriteRepository).findByUserId(fakeUserId);
        verify(recipeRepository, times(3)).findById(isA(String.class));
    }

    @Test
    void createAndReturn() {
        var expected = fakeFavorites().get(0);

        when(favoriteRepository.save(eq(expected))).thenReturn(expected);

        var actual = this.favoriteService.create(expected);

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(favoriteRepository).save(eq(actual));
    }

    @Test
    void findByIdAndReturn() {
        var expected = fakeFavorites().get(0);
        var fakeId = "someFakeId";
        expected.setId(fakeId);

        var recipe = fakeRecipes().get(0);
        recipe.setId("1");

        when(favoriteRepository.findById(fakeId)).thenReturn(Optional.of(expected));
        when(recipeRepository.findById(isA(String.class))).thenReturn(Optional.of(recipe));

        var actualDto = this.favoriteService.findById(fakeId).get();
        var actual = FavoriteMapper.INSTANCE.favoriteDtoToFavorite(actualDto);

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(favoriteRepository).findById(fakeId);
        verify(recipeRepository, times(1)).findById(isA(String.class));
    }

    @Test
    void deleteById() {
        var fakeId = "someFakeId";

        doNothing().when(favoriteRepository).deleteById(fakeId);

        this.favoriteService.deleteById(fakeId);

        verify(favoriteRepository).deleteById(fakeId);
    }
}