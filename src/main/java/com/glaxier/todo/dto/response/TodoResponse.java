package com.glaxier.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TodoResponse {
    int id;
    String description;
    boolean completed;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;
}