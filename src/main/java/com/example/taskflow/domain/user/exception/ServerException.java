package com.example.taskflow.domain.user.exception;

public class ServerException extends RuntimeException{
    public ServerException(String message) {
        super(message);
    }
}
