package com.example.taskflow.domain.common.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다");
    }
}