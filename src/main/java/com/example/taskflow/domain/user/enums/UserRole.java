package com.example.taskflow.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER", "사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한")
    ;

    private final String role;
    private final String description;

}
