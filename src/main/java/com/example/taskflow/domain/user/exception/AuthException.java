package com.example.taskflow.domain.user.exception;

import com.example.taskflow.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AuthException extends BaseException {
    public AuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
