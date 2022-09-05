package com.example.recipeapi.presentation.mapper;

import com.example.recipeapi.business.entity.User;
import com.example.recipeapi.presentation.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);

    default List<UserDto> toUsersDTOs(List<User> users) {
        return users.stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
    }
}
