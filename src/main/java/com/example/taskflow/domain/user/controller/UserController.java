package com.example.taskflow.domain.user.controller;

import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.user.dto.request.DeleteUserRequest;
import com.example.taskflow.domain.user.dto.request.LoginRequest;
import com.example.taskflow.domain.user.dto.request.RegisterRequest;
import com.example.taskflow.domain.user.dto.response.RegisterResponse;
import com.example.taskflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @GetMapping("/users/profile")
    public ResponseEntity myProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.myProfile(userDetails));
    }

    @PostMapping("/users/deletion")
    public ResponseEntity deletion(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DeleteUserRequest deleteUserRequest) {
        return ResponseEntity.ok(userService.deletion(userDetails, deleteUserRequest.getPassword()));
    }

}
