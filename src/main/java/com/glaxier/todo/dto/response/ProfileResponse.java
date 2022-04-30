package com.glaxier.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {
    private String name;
    private String email;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<String> tokens;
}