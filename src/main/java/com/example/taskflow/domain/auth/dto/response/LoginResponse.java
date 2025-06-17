package com.example.taskflow.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String token;

    public LoginResponse(String bearerToken) {
        this.token = bearerToken;
    }

}
