package com.example.recipeapi.business;


import com.example.recipeapi.repository.FavoriteRepository;
import com.example.recipeapi.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAllAndReturn() {
        var expected = fakeUsers();
        when(userRepository.findAll()).thenReturn(expected);
        var actual = this.userService.findAll();

        assertThat(actual.get().size()).isEqualTo(expected.size());
        verify(userRepository).findAll();
    }

    @Test
    void findByIdAndReturn() throws Exception {
        var expected = fakeUsers().get(0);
        var fakeId = "someFakeId";
        expected.setId(fakeId);

        when(userRepository.findById(fakeId)).thenReturn(Optional.of(expected));

        var actual = this.userService.findById(fakeId).get();

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

        verify(userRepository).findById(fakeId);
    }

    @Test
    void createUserAndReturn() {
        var expected = fakeUsers().get(0);

        when(userRepository.save(eq(expected))).thenReturn(expected);

        var actual = this.userService.create(expected);

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

        verify(userRepository).save(eq(actual));
    }

    @Test
    void updateAndReturn() throws Exception {
        var newFirstName = "Jack Sparrow";
        var fakeId = "someFakeId";

        var expected = fakeUsers().get(0);
        expected.setId(fakeId);
        expected.setFname(newFirstName);

        when(userRepository.findById(fakeId)).thenReturn(Optional.of(expected));
        when(userRepository.save(eq(expected))).thenReturn(expected);

        var actual = this.userService.update(expected);

        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(actual.getFname()).isEqualTo(newFirstName);

        verify(userRepository).findById(fakeId);
        verify(userRepository).save(eq(actual));
    }

    @Test
    void deleteById() {
        var fakeId = "someFakeId";

        doNothing().when(userRepository).deleteById(fakeId);
        doNothing().when(favoriteRepository).deleteFavoritesByUserId(fakeId);

        this.userService.deleteById(fakeId);

        verify(userRepository).deleteById(fakeId);
        verify(favoriteRepository).deleteFavoritesByUserId(fakeId);
    }
}