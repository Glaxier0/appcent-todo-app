package com.glaxier.todo.dto.request;

import lombok.*;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTodo {
    @Null(message = "Id update is forbidden.")
    private String id;

    @Size(min = 1, message = "Description length must be minimum 1.")
    private String description;

    private Boolean completed;
}
