package com.example.recipeapi.presentation;

import com.example.recipeapi.presentation.mapper.UserMapper;
import com.example.recipeapi.repository.UserRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.StreamSupport;

import static com.example.recipeapi.dummies.FakeObjects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenUserControllerInjectedThenNotNull() {
        assertThat(userController).isNotNull();
    }

    @Test
    void getAll() throws Exception {
        /***
         * Prepare scenario: fetch all existing users
         * Perform operation
         * Assert result
         */

        this.mockMvc.perform(get("/user"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThan(1)));
    }

    @Test
    void findById() throws Exception {
        /***
         * Prepare scenario: first fetch an existing user then use its userId to test the function
         * Perform operation
         * Assert result
         */

        var user = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).toList().get(0);

        this.mockMvc.perform(get("/user/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.fname").value(user.getFname()))
                .andExpect(jsonPath("$.lname").value(user.getLname()));
    }

    @Test
    void createAndReturn() throws Exception {
        /***
         * Prepare scenario: first create a dummy user then post
         * Perform operation
         * Assert result
         */

        var fakeUserDto = UserMapper.INSTANCE.userToUserDto(fakeUsers().get(0));
        var requestJson = new Gson().toJson(fakeUserDto);

        this.mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.fname").value(fakeUserDto.getFname()))
                .andExpect(jsonPath("$.lname").value(fakeUserDto.getLname()));
    }

    @Test
    void deleteById() throws Exception {
        /***
         * Prepare scenario: first fetch an existing user then delete by using its id
         * Perform operation
         * Assert result
         */

        var user = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).toList().get(0);

        this.mockMvc.perform(delete("/user/{id}", user.getId()))
                .andExpect(status().isOk());

        assertThat(this.userRepository.findById(user.getId()).isEmpty()).isTrue();
    }
}