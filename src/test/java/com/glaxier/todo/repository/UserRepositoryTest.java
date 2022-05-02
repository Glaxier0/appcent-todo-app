package com.glaxier.todo.repository;

import com.glaxier.todo.model.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void testCRUD() {
        Users user = new Users("test", "test@test.com", "testtest");

        userRepository.save(user);

        Iterable<Users> usersList = userRepository.findAll();
        Assertions.assertThat(usersList).extracting(Users::getEmail).containsOnly("test@test.com");

        user.setName("updatetest");
        userRepository.save(user);

        usersList = userRepository.findAll();
        Assertions.assertThat(usersList).extracting(Users::getName).containsOnly("updatetest");

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }
}