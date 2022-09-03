package com.example.recipeapi.presentation;

import com.example.recipeapi.presentation.dto.PageDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public abstract class BaseController<R> {
    abstract Optional<R> findById(@PathVariable String id) throws Exception;
    abstract Optional<PageDto<R>> queryPage(@RequestParam int page);
    abstract Optional<PageDto<R>> searchBooksByTitle(@RequestParam String q, @RequestParam int page);
    abstract Optional<PageDto<R>> searchBooksByAmountPages(@RequestParam String q, @RequestParam int page);
}
