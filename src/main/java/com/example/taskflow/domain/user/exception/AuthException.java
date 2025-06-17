package com.example.taskflow.domain.user.exception;

import com.example.taskflow.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AuthException extends BaseException {

    /**
     * 인증 예외처리
     *
     * @param message 예외 안내 문구
     * @return 401 UNAUTHORIZED, 예외 안내 문구
     */
    public AuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
