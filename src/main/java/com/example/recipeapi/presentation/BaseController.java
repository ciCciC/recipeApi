package com.example.recipeapi.presentation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public abstract class BaseController<R> {

    abstract Optional<R> findById(@PathVariable String id) throws Exception;
    abstract R create(@RequestBody R object);
    abstract void deleteById(@PathVariable String id);

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
