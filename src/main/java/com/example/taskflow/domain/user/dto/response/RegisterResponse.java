package com.example.taskflow.domain.user.dto.response;

import lombok.Getter;

@Getter
public class RegisterResponse {

    private final String bearerToken;

    public RegisterResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }

}
