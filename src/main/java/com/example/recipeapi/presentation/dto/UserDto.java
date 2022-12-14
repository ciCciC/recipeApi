package com.example.recipeapi.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    @NotBlank(message = "Is mandatory")
    private String fname;
    @NotBlank(message = "Is mandatory")
    private String lname;
}
