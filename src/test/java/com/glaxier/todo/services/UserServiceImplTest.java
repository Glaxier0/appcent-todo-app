package com.glaxier.todo.services;

import com.glaxier.todo.model.Users;
import com.glaxier.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findAll_shouldReturnSameSize() {
        List<Users> usersList = Arrays.asList(new Users(1, "test", "testtest"),
                new Users(2, "testtest", "testtest"));

        when(userRepository.findAll()).thenReturn(usersList);
        List<Users> users = userService.findAll();
        assertEquals(users.size(), usersList.size());
    }

    @Test
    void findById_shouldReturnSameUser() {
        Users user = new Users(1, "test", "testtest");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Users result = userService.findById(1).get();
        assertEquals(result, user);
    }

    @Test
    void deleteAll() {
        userService.deleteAll();
        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    void save_shouldReturnSameUser() {
        Users user = new Users(1, "test", "testtest");

        when(userRepository.save(any())).thenReturn(user);
        Users result = userService.save(user);
        assertEquals(result, user);
    }

    @Test
    void deleteById() {
        userService.deleteById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void findByEmail_shouldReturnSameUser() {
        Users user = new Users(1, "test", "testtest");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Users result = userService.findByEmail(user.getEmail()).get();
        assertEquals(result, user);
    }
}