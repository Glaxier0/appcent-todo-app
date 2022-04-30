package com.glaxier.todo.util;

import com.glaxier.todo.dto.request.UpdateTodo;
import com.glaxier.todo.dto.request.UpdateUser;
import com.glaxier.todo.model.Todo;
import com.glaxier.todo.model.Users;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class PartialUpdate {
    private Validator validator;

    public Optional<Todo> todoPartialUpdate(UpdateTodo task, Optional<Todo> todoData) {
        todoData.get().updateTask(task);
        validateTodo(todoData.get());
        return todoData;
    }

    public void validateTodo(Todo todo) {
        Set<ConstraintViolation<Todo>> violations = validator.validate(todo);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Optional<Users> userPartialUpdate(UpdateUser user, Optional<Users> userData) {
        userData.get().updateUser(user);
        validateUser(userData.get());
        return userData;
    }

    public void validateUser(Users user) {
        Set<ConstraintViolation<Users>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
