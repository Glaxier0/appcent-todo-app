package com.glaxier.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glaxier.todo.dto.request.UpdateUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@Schema
@NoArgsConstructor
public class Users {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "Name is required.")
    @Size(max = 16)
    private String name;

    @Column
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 64)
    private String email;

    @Column
    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 128, message = "Password length must be minimum 8.")
    private String password;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "tokens")
    @ElementCollection(targetClass=String.class)
    private List<String> tokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Todo> todos = new ArrayList<>();

    public Users(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateUser(UpdateUser updateUser) {
        this.name = updateUser.getName() == null ? this.name : updateUser.getName();
        this.password = updateUser.getPassword() == null ? this.password : updateUser.getPassword();
    }
}