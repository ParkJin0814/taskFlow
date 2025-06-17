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

    /**
     * 회원 가입
     *
     * @param registerRequest 회원 가입 작성 폼 (username, email, password, name)
     * @return 회원 가입 된 유저 정보
     */
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    /**
     * 로그인
     *
     * @param loginRequest 로그인 작성 폼 (username, password)
     * @return 로그인 된 유저 JWT토큰
     */
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    /**
     * 내 프로필
     *
     * @param userDetails 로그인 된 유저 JWT토큰의 정보 (username 등)
     * @return 로그인 된 유저 정보
     */
    @GetMapping("/users/profile")
    public ResponseEntity myProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.myProfile(userDetails));
    }

    /**
     * 회원 탈퇴
     *
     * @param userDetails 로그인 된 유저 JWT토큰의 정보 (username 등)
     * @param deleteUserRequest 유저 입력 작성 폼 (password)
     * @return 회원 탈퇴 완료 메시지
     */
    @PostMapping("/users/deletion")
    public ResponseEntity deletion(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DeleteUserRequest deleteUserRequest) {
        return ResponseEntity.ok(userService.deletion(userDetails, deleteUserRequest.getPassword()));
    }

}
