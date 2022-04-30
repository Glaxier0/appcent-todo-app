package com.glaxier.todo.services;

import com.glaxier.todo.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<Todo> findAll();

    Optional<Todo> findById(Integer id);

    Optional<Todo> findByIdAndUserId(int id, int userId);

    void deleteAll();
    void deleteAllByUserId(int userId);

    Todo save(Todo todo);

    void deleteById(Integer id);
}
