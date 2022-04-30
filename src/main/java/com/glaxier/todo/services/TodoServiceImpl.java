package com.glaxier.todo.services;

import com.glaxier.todo.model.Todo;
import com.glaxier.todo.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {
    TodoRepository todoRepository;

    @Override
    public List<Todo> findAll() {
        List<Todo> todos = new ArrayList<>();
        todoRepository.findAll().iterator().forEachRemaining(todos::add);
        return todos;
    }

    @Override
    public Optional<Todo> findById(Integer id) {
        return todoRepository.findById(id);
    }

    @Override
    public Optional<Todo> findByIdAndUserId(int id, int userId) {
        return todoRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public void deleteAll() {
        todoRepository.deleteAll();
    }

    @Override
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public void deleteById(Integer id) {
        todoRepository.deleteById(id);
    }

    @Override
    public void deleteAllByUserId(int userId) {
        todoRepository.deleteAllByUserId(userId);
    }
}
