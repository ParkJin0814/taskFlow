package com.example.taskflow.domain.auth.controller;

import com.example.taskflow.global.config.aop.Logging;
import com.example.taskflow.domain.auth.dto.request.LoginRequest;
import com.example.taskflow.domain.auth.dto.request.RegisterRequest;
import com.example.taskflow.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 회원 가입
     *
     * @param registerRequest 회원 가입 작성 폼 (username, email, password, name)
     * @return 회원 가입 된 유저 정보
     */
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    /**
     * 로그인
     *
     * @param loginRequest 로그인 작성 폼 (username, password)
     * @return 로그인 된 유저 JWT토큰
     */
    @PostMapping("/login")
    @Logging
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
