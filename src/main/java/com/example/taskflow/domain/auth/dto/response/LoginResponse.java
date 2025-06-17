package com.example.taskflow.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {


    //프론트에서 원하는 형태는 bearToken이 아니고 token
    private final String token;

    public LoginResponse(String bearerToken) {
        this.token = bearerToken;
    }

}
