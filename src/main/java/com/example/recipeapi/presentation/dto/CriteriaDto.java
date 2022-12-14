package com.example.recipeapi.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaDto {
    @NotBlank
    private String fieldName;
    @NotBlank
    private String value;
    @NotBlank
    private boolean condition;
}
