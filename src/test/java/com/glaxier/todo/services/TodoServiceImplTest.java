package com.glaxier.todo.services;

import com.glaxier.todo.model.Todo;
import com.glaxier.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @Mock
    TodoRepository todoRepository;
    @InjectMocks
    TodoServiceImpl todoService;

    @Test
    void findById_shouldReturnSameDescription() {
        int id = new Random().nextInt();
        Todo todo = new Todo(id, "Do unit test.");
        when(todoRepository.findById(any())).thenReturn(Optional.of(todo));

        Optional<Todo> result = todoService.findById(id);
        assertThat(result.get().getDescription()).isEqualTo("Do unit test.");
    }

    @Test
    void findAll_shouldReturnSameSize() {
        List<Todo> todoList = Arrays.asList(new Todo(1, "Do unit test."),
                new Todo(2, "Do todo app."));

        when(todoRepository.findAll()).thenReturn(todoList);
        List<Todo> todos = todoService.findAll();
        assertEquals(todoList.size(), todos.size());
    }

    @Test
    void findByIdAndUserId_shouldReturnSameIds() {
        int id = 1;
        Todo todo = new Todo(id, "Do unit test");

        when(todoRepository.findByIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.of(todo));
        Todo result = todoService.findByIdAndUserId(id, id).get();
        assertEquals(result.getId(), todo.getId());
    }

    @Test
    void deleteAll() {
        todoService.deleteAll();
        verify(todoRepository, times(1)).deleteAll();
    }

    @Test
    void save_shouldReturnSameDescription() {
        Todo todo = new Todo(1, "Do unit test.");

        when(todoRepository.save(any())).thenReturn(todo);
        Todo result = todoService.save(todo);
        assertEquals(result.getDescription(), todo.getDescription());
    }

    @Test
    void deleteById() {
        todoService.deleteById(1);
        verify(todoRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteAllByUserId() {
        todoService.deleteAllByUserId(1);
        verify(todoRepository, times(1)).deleteAllByUserId(1);
    }
}