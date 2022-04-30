package com.glaxier.todo.dto.request;

import lombok.*;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {
    @Null(message = "Id update is forbidden.")
    private String id;

    @Size(min = 1, max = 16, message = "Name length must be minimum 1.")
    private String name;

    @Null(message = "Email update is forbidden.")
    private String email;

    @Size(min = 8, max = 128, message = "Password length must be minimum 8.")
    private String password;
}
