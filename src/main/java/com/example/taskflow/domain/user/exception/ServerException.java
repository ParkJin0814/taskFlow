package com.example.taskflow.domain.user.exception;

import com.example.taskflow.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ServerException extends BaseException {

    /**
     * 서버에러 예외처리
     *
     * @param message 예외 안내 문구
     * @return 500 INTERNAL_SERVER_ERROR, 예외 안내 문구
     */
    public ServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR,message);
    }
}
