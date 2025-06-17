package com.example.taskflow.domain.user.controller;

import com.example.taskflow.domain.user.dto.request.DeleteUserRequest;
import com.example.taskflow.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 내 프로필
     *
     * @param userDetails 로그인 된 유저 JWT토큰의 정보 (username 등)
     * @return 로그인 된 유저 정보
     */
    @GetMapping("/users/me")
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
