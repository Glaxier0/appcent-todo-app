package com.glaxier.todo.repository;

import com.glaxier.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    Optional<Todo> findByIdAndUserId(int id, int userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM todos WHERE user_id=:userId", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") int userId);
}
