package com.glaxier.todo.repository;

import com.glaxier.todo.model.Todo;
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
class TodoRepositoryTest {
    @Autowired
    TodoRepository todoRepository;

    @Test
    void testCRUD() {
        Todo todo = new Todo("Do unit test.", false);

        todoRepository.save(todo);

        Iterable<Todo> todos = todoRepository.findAll();
        Assertions.assertThat(todos).extracting(Todo::getDescription).containsOnly("Do unit test.");

        todo.setCompleted(true);
        todoRepository.save(todo);

        todos = todoRepository.findAll();
        Assertions.assertThat(todos).extracting(Todo::isCompleted).containsOnly(true);

        todoRepository.deleteAll();
        Assertions.assertThat(todoRepository.findAll()).isEmpty();
    }
}