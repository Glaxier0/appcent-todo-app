package com.glaxier.todo.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoRequest {
    @NotBlank(message = "Description required")
    String description;
    boolean completed = false;
}
