package com.glaxier.todo.controller;

import com.glaxier.todo.dto.request.UpdateTodo;
import com.glaxier.todo.model.Todo;
import com.glaxier.todo.model.Users;
import com.glaxier.todo.security.jwt.JwtUtils;
import com.glaxier.todo.services.TodoService;
import com.glaxier.todo.services.UserDetailsImpl;
import com.glaxier.todo.services.UserService;
import com.glaxier.todo.util.PartialUpdate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class TodoController {
    TodoService todoService;
    UserService userService;
    JwtUtils jwtUtils;
    PartialUpdate partialUpdate;

    @PostMapping("/todos")
    public ResponseEntity<Todo> saveTodo(@RequestBody @Valid Todo todo) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> users = userService.findById(userDetails.getId());
        if (users.isPresent()) {
            todo.setUser(users.get());
            users.get().getTodos().add(todo);
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        return new ResponseEntity<>(todoService.save(todo), HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> users = userService.findById(userDetails.getId());

        List<Todo> todos = users.get().getTodos();
        if (todos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users.get().getTodos(), HttpStatus.OK);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTask(@PathVariable("id") int id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();
        Optional<Todo> todoData = todoService.findByIdAndUserId(id, userId);
        if (todoData.isPresent()) {
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTask(@PathVariable("id") int id, @RequestBody @Valid UpdateTodo todo) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();
        Optional<Todo> todoData = todoService.findByIdAndUserId(id, userId);

        if (todoData.isPresent()) {
            todoData = partialUpdate.todoPartialUpdate(todo, todoData);
            todoData.get().setUpdatedAt(ZonedDateTime.now(ZoneOffset.UTC));
            todoService.save(todoData.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") int id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();
        Optional<Todo> todoData = todoService.findByIdAndUserId(id, userId);

        if (todoData.isPresent()) {
            todoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos")
    public ResponseEntity<HttpStatus> deleteAllTasks() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        try {
            todoService.deleteAllByUserId(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}