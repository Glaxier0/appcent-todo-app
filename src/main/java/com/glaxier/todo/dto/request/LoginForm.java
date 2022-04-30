package com.glaxier.todo.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 64)
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 128, message = "Password length must be minimum 8.")
    private String password;
}
