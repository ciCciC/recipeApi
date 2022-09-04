package com.example.recipeapi.presentation;

import com.example.recipeapi.business.DataLoaderService;
import com.example.recipeapi.business.UserService;
import com.example.recipeapi.document.User;
import com.example.recipeapi.presentation.dto.PageDto;
import com.example.recipeapi.presentation.dto.UserDto;
import com.example.recipeapi.presentation.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserDto> {

    private final UserService userService;
    private final DataLoaderService dataLoaderService;

    public UserController(UserService userService, DataLoaderService dataLoaderService) {
        this.userService = userService;
        this.dataLoaderService = dataLoaderService;
        this.dataLoaderService.removeAll(User.class);
        this.dataLoaderService.populateUsers();
    }

    @GetMapping()
    @ResponseBody
    @Override
    public Optional<List<UserDto>> getAll() {
        var users = this.userService.findAll();
        var userDtos = users.map(UserMapper.INSTANCE::toUsersDTOs);
        return userDtos;
    }

    @Override
    Optional<UserDto> findById(String id) throws Exception {
        var user = this.userService.findById(id);
        return user.map(UserMapper.INSTANCE::userToUserDto);
    }

    @Override
    Optional<PageDto<UserDto>> queryPage(int page) {
        return Optional.empty();
    }

    @Override
    Optional<PageDto<UserDto>> searchBooksByTitle(String q, int page) {
        return Optional.empty();
    }

    @Override
    Optional<PageDto<UserDto>> searchBooksByAmountPages(String q, int page) {
        return Optional.empty();
    }
}
