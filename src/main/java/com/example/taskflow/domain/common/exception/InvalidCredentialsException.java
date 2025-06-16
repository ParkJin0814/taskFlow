package com.example.taskflow.domain.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "잘못된 사용자명 또는 비밀번호입니다");
    }
}