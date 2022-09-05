package com.example.recipeapi.presentation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public abstract class BaseController<R> {

    abstract Optional<R> findById(@PathVariable String id) throws Exception;
    abstract R create(@RequestBody R object);
    abstract void deleteById(@PathVariable String id);
//    abstract Optional<PageDto<R>> queryPage(@RequestParam int page);
//    abstract Optional<PageDto<R>> searchBooksByTitle(@RequestParam String q, @RequestParam int page);
//    abstract Optional<PageDto<R>> searchBooksByAmountPages(@RequestParam String q, @RequestParam int page);

    R update(@RequestBody R object) throws Exception {
        return null;
    };
    Optional<List<R>> getAll(){
        return null;
    };
    Optional<List<R>> getAll(String id) {
        return null;
    }
}
