package com.glaxier.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "todos")
@NoArgsConstructor
public class Todo {
    @Id
    @Column(name = "todo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "Description required")
    private String description;

    @Column
    private boolean completed = false;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public Todo(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Todo(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }
}
