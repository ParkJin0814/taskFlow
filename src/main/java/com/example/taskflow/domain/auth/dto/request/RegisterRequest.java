package com.example.taskflow.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private final String username;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "새 비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "새 비밀번호는 영문자를 포함해야 합니다.")
    @Pattern(regexp = ".*\\d.*", message = "새 비밀번호는 숫자를 포함해야 합니다.")
    @Pattern(regexp = ".*[`~!@#$%^&*].*", message = "새 비밀번호는 특수문자를 포함해야 합니다.")
    private final String password;
    // 프론트에서만 2중으로 검증하지 요청할 때 필요 치 않은 값.
//    @NotBlank(message = "비밀번호가 일치하지 않습니다.")
//    private final String passwordCheck;

    @NotBlank(message = "이름을 입력해주세요.")
    private final String name;

}
