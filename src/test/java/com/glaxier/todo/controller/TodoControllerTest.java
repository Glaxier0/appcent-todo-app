package com.glaxier.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaxier.todo.WithMockCustomUser;
import com.glaxier.todo.dto.request.TodoRequest;
import com.glaxier.todo.dto.request.UpdateTodo;
import com.glaxier.todo.model.Todo;
import com.glaxier.todo.model.Users;
import com.glaxier.todo.security.jwt.AuthEntryPointJwt;
import com.glaxier.todo.security.jwt.JwtUtils;
import com.glaxier.todo.services.TodoService;
import com.glaxier.todo.services.UserDetailsServiceImpl;
import com.glaxier.todo.services.UserService;
import com.glaxier.todo.util.PartialUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
@WithMockCustomUser
class TodoControllerTest {
    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    TodoService todoService;

    @MockBean
    UserService userService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    PartialUpdate partialUpdate;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveTodo_shouldReturnOk_andSameDescription() throws Exception {
        TodoRequest todoRequest = new TodoRequest("Do unit test.", false);
        Todo todo = new Todo(todoRequest.getDescription(), todoRequest.isCompleted());
        when(todoService.save(any())).thenReturn(todo);
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("description").value(todoRequest.getDescription()))
                .andDo(print());
    }

    @Test
    void getTodos_shouldReturnSizeofUserTodos() throws Exception {
        List<Todo> todoList = Arrays.asList(new Todo("Do unit test.", true),
                new Todo("Do unit test.", false));
        Users user = new Users("test", "test@test.com", "testtest");
        user.setTodos(todoList);
        when(userService.findById(anyInt())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(user.getTodos().size())))
                .andDo(print());
    }

    @Test
    void getTodo_shouldReturnTodoWithUserId_andOk() throws Exception {
        int id = 1;
        Todo todo = new Todo(id, "Do unit test.");
        Users user = new Users(id, "test@test.com", "testtest");
        todo.setUser(user);

        when(todoService.findByIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.of(todo));
        mockMvc.perform(get("/todos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("description").value("Do unit test."))
                .andExpect(jsonPath("user.id").value(id))
                .andDo(print());
    }

    @Test
    void updateTask_shouldReturnOk() throws Exception {
        int id = 1;
        UpdateTodo updateTodo = new UpdateTodo("Unit test done", true);

        when(todoService.findByIdAndUserId(anyInt(), anyInt()))
                .thenReturn(Optional.of(new Todo(id, "Do unit test.")));
        when(partialUpdate.todoPartialUpdate(any(), any())).thenReturn(new Todo(1, updateTodo.getDescription()));
        mockMvc.perform(patch("/todos/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTodo)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteTask_shouldReturnOk() throws Exception {
        int id = 1;
        when(todoService.findByIdAndUserId(anyInt(), anyInt()))
                .thenReturn(Optional.of(new Todo(id, "Do unit test.")));
        mockMvc.perform(delete("/todos/" + id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteAllTasks_shouldReturnOk() throws Exception {
        int userId = 1;
        doNothing().when(todoService).deleteAllByUserId(userId);
        mockMvc.perform(delete("/todos/"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}