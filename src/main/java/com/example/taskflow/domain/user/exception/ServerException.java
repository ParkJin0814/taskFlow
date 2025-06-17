package com.example.taskflow.domain.user.exception;

import com.example.taskflow.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ServerException extends BaseException {
    public ServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR,message);
    }
}
