package com.glaxier.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RegisterResponse {
    private String name;
    private String email;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
