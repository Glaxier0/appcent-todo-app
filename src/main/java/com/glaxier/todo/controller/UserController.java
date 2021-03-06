package com.glaxier.todo.controller;

import com.glaxier.todo.dto.request.LoginForm;
import com.glaxier.todo.dto.request.RegisterForm;
import com.glaxier.todo.dto.request.UpdateUser;
import com.glaxier.todo.dto.response.JwtResponse;
import com.glaxier.todo.dto.response.ProfileResponse;
import com.glaxier.todo.dto.response.RegisterResponse;
import com.glaxier.todo.model.Users;
import com.glaxier.todo.security.jwt.JwtUtils;
import com.glaxier.todo.services.UserDetailsImpl;
import com.glaxier.todo.services.UserService;
import com.glaxier.todo.util.PartialUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {
    JwtUtils jwtUtils;
    UserService userService;
    PartialUpdate partialUpdate;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;

    @PostMapping("/users/register")
    public ResponseEntity<RegisterResponse> saveUser(@RequestBody @Valid RegisterForm registerForm) {
        if (userService.existsByEmail(registerForm.getEmail())) {
            throw new DuplicateKeyException("Email already in use!");
        }

        Users user = new Users(registerForm.getName(), registerForm.getEmail(),
                passwordEncoder.encode(registerForm.getPassword()));
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userService.save(user);
        return new ResponseEntity<>(new RegisterResponse(user.getName(), user.getEmail(),
                user.getCreatedAt(), user.getUpdatedAt()), HttpStatus.CREATED);
    }

    @PostMapping("/users/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Users user = userService.findByEmail(userDetails.getEmail()).get();
        user.getTokens().add(token);
        userService.save(user);

        return new ResponseEntity<>(new JwtResponse(user.getName(),
                user.getEmail(), token), HttpStatus.OK);
    }

    @PostMapping("/users/logout")
    @Operation(description = "Deletes jwt token attached to user",
            security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<HttpStatus> logout(@RequestHeader HttpHeaders httpHeaders) {
        String token = jwtUtils.getToken(httpHeaders);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findById(userDetails.getId()).get();
        user.getTokens().remove(token);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/logoutAll")
    @Operation(description = "Deletes all jwt tokens attached to user",
            security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<HttpStatus> logoutAll() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findById(userDetails.getId()).get();
        user.setTokens(new ArrayList<>());
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/me")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<ProfileResponse> getProfile() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findById(userDetails.getId()).get();
        return new ResponseEntity<>(new ProfileResponse(user.getName(), user.getEmail(),
                user.getCreatedAt(), user.getUpdatedAt(), user.getTokens()), HttpStatus.OK);
    }

    @PatchMapping("/users/me")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<HttpStatus> updateProfile(@RequestBody @Valid UpdateUser updateUser) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> userData = userService.findById(userDetails.getId());

        if (updateUser.getPassword() != null) {
            updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }
        if (userData.isPresent()) {
            Users user = userData.get();
            user = partialUpdate.userPartialUpdate(updateUser, user);
            user.setUpdatedAt(ZonedDateTime.now(ZoneOffset.UTC));
            userService.save(user);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<HttpStatus> deleteProfile() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            userService.deleteById(userDetails.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}