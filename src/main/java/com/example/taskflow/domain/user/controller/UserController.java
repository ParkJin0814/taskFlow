package com.example.taskflow.domain.user.controller;

import com.example.taskflow.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
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
    @GetMapping("/me")
    public ResponseEntity myProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.myProfile(userDetails));
    }

    @GetMapping
    public ResponseEntity searchUsers(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.myProfile(userDetails));
    }

}
