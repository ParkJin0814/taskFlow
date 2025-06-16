package com.example.taskflow.domain.user.controller;

import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.user.dto.request.DeleteUserRequest;
import com.example.taskflow.domain.user.dto.request.LoginRequest;
import com.example.taskflow.domain.user.dto.request.RegisterRequest;
import com.example.taskflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @GetMapping("/users/profile")
    public ApiResponse myProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.myProfile(userDetails);
    }

    @PostMapping("/users/deletion")
    public ApiResponse deletion(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DeleteUserRequest deleteUserRequest) {
        return userService.deletion(userDetails, deleteUserRequest.getPassword());
    }

}
