package com.example.recipeapi.presentation;

import com.example.recipeapi.business.UserService;
import com.example.recipeapi.presentation.dto.UserDto;
import com.example.recipeapi.presentation.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserDto> {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseBody
    @Override
    public Optional<List<UserDto>> getAll() {
        var users = this.userService.findAll();
        return users.map(UserMapper.INSTANCE::toUsersDTOs);
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
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        var user = this.userService.create(UserMapper.INSTANCE.userDtoToUser(userDto));
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public void deleteById(@PathVariable String id) {
        this.userService.deleteById(id);
    }
}
