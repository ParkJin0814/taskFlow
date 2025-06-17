package com.example.taskflow.domain.common.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BaseException {
    public UsernameAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다");
    }
}