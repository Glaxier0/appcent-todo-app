package com.glaxier.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glaxier.todo.dto.request.UpdateTodo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @Column(name = "todo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "Description required")
    private String description;

    @Column(name = "completed")
    private boolean isCompleted = false;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    public void updateTask(UpdateTodo updateTodo) {
        this.description = updateTodo.getDescription() == null ? this.description : updateTodo.getDescription();
        this.isCompleted = updateTodo.getCompleted() == null ? this.isCompleted : updateTodo.getCompleted();
    }
}
