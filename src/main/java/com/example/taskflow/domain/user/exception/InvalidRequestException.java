package com.example.taskflow.domain.user.exception;

import com.example.taskflow.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
