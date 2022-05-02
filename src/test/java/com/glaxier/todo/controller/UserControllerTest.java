package com.glaxier.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaxier.todo.WithMockCustomUser;
import com.glaxier.todo.dto.request.LoginForm;
import com.glaxier.todo.dto.request.RegisterForm;
import com.glaxier.todo.dto.request.UpdateUser;
import com.glaxier.todo.model.Users;
import com.glaxier.todo.security.jwt.AuthEntryPointJwt;
import com.glaxier.todo.security.jwt.JwtUtils;
import com.glaxier.todo.services.UserDetailsImpl;
import com.glaxier.todo.services.UserDetailsServiceImpl;
import com.glaxier.todo.services.UserService;
import com.glaxier.todo.util.PartialUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    UserService userService;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    PartialUpdate partialUpdate;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SecurityContext securityContext;

    @Test
    void saveUser_should_return_created() throws Exception {
        RegisterForm registerForm = new RegisterForm("test", "test@test.com", "testtest");
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerForm)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void login_should_return_jwt_token() throws Exception {
        Users user = new Users("test", "test@test.com", "testtest");
        LoginForm loginForm = new LoginForm(user.getEmail(), user.getPassword());
        when(userDetailsService.loadUserByUsername("test@test.com"))
                .thenReturn(new UserDetailsImpl(1, "test", "test@test.com", "testtest"));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtUtils.generateJwtToken(any())).thenReturn("success");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value("success"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    void logout_should_return_ok() throws Exception {
        Users user = new Users(1, "test@test.com", "testtest");
        when(userService.findById(anyInt())).thenReturn(Optional.of(user));
        when(userService.save(any())).thenReturn(new Users());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth("success");
        mockMvc.perform(post("/users/logout").headers(httpHeaders)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockCustomUser
    void logoutAll_should_return_200() throws Exception {
        Users user = new Users(1, "test@test.com", "testtest");
        when(userService.findById(anyInt())).thenReturn(Optional.of(user));
        mockMvc.perform(post("/users/logoutAll")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockCustomUser
    void getProfile_should_return_same_email() throws Exception {
        Users user = new Users(1, "test@test.com", "testtest");
        when(userService.findById(anyInt())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/me")).andExpect(status().isOk())
                .andExpect(jsonPath("email").value(user.getEmail())).andDo(print());
    }

    @Test
    @WithMockCustomUser
    void updateProfile_should_return_ok() throws Exception {
        Users user = new Users(1, "test@test.com", "testtest");
        UpdateUser updateUser = new UpdateUser("test2", "test12345");
        when(userService.findById(anyInt())).thenReturn(Optional.of(user));
        when(partialUpdate.userPartialUpdate(any(), any())).
                thenReturn(new Users(updateUser.getName(), user.getEmail(), updateUser.getPassword()));
        mockMvc.perform(patch("/users/me").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    void deleteProfile() throws Exception {
        doNothing().when(userService).deleteById(any());
        mockMvc.perform(delete("/users/me"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}