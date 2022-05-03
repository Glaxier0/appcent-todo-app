package com.glaxier.todo.controller;

import com.glaxier.todo.dto.request.TodoRequest;
import com.glaxier.todo.dto.request.UpdateTodo;
import com.glaxier.todo.dto.response.TodoResponse;
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
import java.util.ArrayList;
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
    public ResponseEntity<TodoResponse> saveTodo(@RequestBody @Valid TodoRequest todoRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> users = userService.findById(userDetails.getId());
        Todo todo = new Todo(todoRequest.getDescription(), todoRequest.isCompleted());
        if (users.isPresent()) {
            todo.setUser(users.get());
            users.get().getTodos().add(todo);
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        todoService.save(todo);
        return new ResponseEntity<>(new TodoResponse(todo.getId(), todo.getDescription(),
                todo.isCompleted(), todo.getCreatedAt(), todo.getUpdatedAt()), HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<TodoResponse>> getTodos() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> users = userService.findById(userDetails.getId());

        List<Todo> todoList = users.get().getTodos();
        List<TodoResponse> todoResponseList = new ArrayList<>();
        for (Todo todo : todoList) {
            TodoResponse todoResponse = new TodoResponse(todo.getId(), todo.getDescription(), todo.isCompleted(),
                    todo.getCreatedAt(), todo.getUpdatedAt());
            todoResponseList.add(todoResponse);
        }
        if (todoResponseList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(todoResponseList, HttpStatus.OK);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable("id") int id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();
        Optional<Todo> todoData = todoService.findByIdAndUserId(id, userId);
        if (todoData.isPresent()) {
            Todo todo = todoData.get();
            TodoResponse todoResponse = new TodoResponse(todo.getId(), todo.getDescription(), todo.isCompleted(),
                    todo.getCreatedAt(), todo.getUpdatedAt());
            return new ResponseEntity<>(todoResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/todos/{id}")
    public ResponseEntity<HttpStatus> updateTask(@PathVariable("id") int id, @RequestBody @Valid UpdateTodo updateTodo) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();
        Optional<Todo> todoData = todoService.findByIdAndUserId(id, userId);

        if (todoData.isPresent()) {
            Todo todo = todoData.get();
            todo = partialUpdate.todoPartialUpdate(updateTodo, todo);
            todo.setUpdatedAt(ZonedDateTime.now(ZoneOffset.UTC));
            todoService.save(todo);
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