package com.example.taskflow.domain.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestFormatException extends BaseException {
    public BadRequestFormatException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 요청입니다");
    }
}