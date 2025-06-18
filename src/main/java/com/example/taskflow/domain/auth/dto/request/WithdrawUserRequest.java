package com.example.taskflow.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WithdrawUserRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "비밀번호는 영문자를 포함해야 합니다.")
    @Pattern(regexp = ".*\\d.*", message = "비밀번호는 숫자를 포함해야 합니다.")
    @Pattern(regexp = ".*[`~!@#$%^&*].*", message = "비밀번호는 특수문자를 포함해야 합니다.")
    private String password;

}
