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
import java.util.Set;

@Component
@AllArgsConstructor
public class PartialUpdate {
    private Validator validator;

    public Todo todoPartialUpdate(UpdateTodo updateTodo, Todo todo) {
        todo.setDescription(updateTodo.getDescription() == null ? todo.getDescription() : updateTodo.getDescription());
        todo.setCompleted(updateTodo.getCompleted() == null ? todo.isCompleted() : updateTodo.getCompleted());
        validateTodo(todo);
        return todo;
    }

    public void validateTodo(Todo todo) {
        Set<ConstraintViolation<Todo>> violations = validator.validate(todo);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Users userPartialUpdate(UpdateUser updateUser, Users user) {
        user.setName(updateUser.getName() == null ? user.getName() : updateUser.getName());
        user.setPassword(updateUser.getPassword() == null ? user.getPassword() : updateUser.getPassword());
        validateUser(user);
        return user;
    }

    public void validateUser(Users user) {
        Set<ConstraintViolation<Users>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
