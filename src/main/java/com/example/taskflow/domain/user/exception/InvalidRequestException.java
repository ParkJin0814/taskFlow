package com.example.taskflow.domain.user.exception;

import com.example.taskflow.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BaseException {

    /**
     * 잘못된 요청 예외처리
     *
     * @param message 예외 안내 문구
     * @return 400 BAD_REQUEST, 예외 안내 문구
     */
    public InvalidRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
