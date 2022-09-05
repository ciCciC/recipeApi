package com.example.recipeapi.presentation;

import com.example.recipeapi.business.DataLoaderService;
import com.example.recipeapi.business.UserService;
import com.example.recipeapi.document.User;
import com.example.recipeapi.presentation.dto.UserDto;
import com.example.recipeapi.presentation.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserDto> {

    private final UserService userService;

    public UserController(UserService userService, DataLoaderService dataLoaderService) {
        this.userService = userService;
        dataLoaderService.removeAll(User.class);
        dataLoaderService.populateUsers();
    }

    @GetMapping
    @ResponseBody
    @Override
    public Optional<List<UserDto>> getAll() {
        var users = this.userService.findAll();
        var userDtos = users.map(UserMapper.INSTANCE::toUsersDTOs);
        return userDtos;
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Optional<UserDto> findById(String id) throws Exception {
        var user = this.userService.findById(id);
        return user.map(UserMapper.INSTANCE::userToUserDto);
    }

    @PostMapping
    @ResponseBody
    @Override
    public UserDto create(@RequestBody UserDto userDto) {
        var user = this.userService.create(UserMapper.INSTANCE.userDtoToUser(userDto));
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public void deleteById(@PathVariable String id) {
        this.userService.deleteById(id);
    }

//    @Override
//    Optional<PageDto<UserDto>> queryPage(int page) {
//        return Optional.empty();
//    }
//
//    @Override
//    Optional<PageDto<UserDto>> searchBooksByTitle(String q, int page) {
//        return Optional.empty();
//    }
//
//    @Override
//    Optional<PageDto<UserDto>> searchBooksByAmountPages(String q, int page) {
//        return Optional.empty();
//    }
}
