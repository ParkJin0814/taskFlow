package com.example.taskflow.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WithdrawUserRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
