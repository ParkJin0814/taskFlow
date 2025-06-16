package com.example.taskflow.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private final String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

}
