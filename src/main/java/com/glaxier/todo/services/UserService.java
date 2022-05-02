package com.glaxier.todo.services;

import com.glaxier.todo.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<Users> findAll();

    Optional<Users> findById(Integer id);

    void deleteAll();

    Users save(Users user);

    void deleteById(Integer id);

    Optional<Users> findByEmail(String email);

    Boolean existsByEmail(String email);
}
