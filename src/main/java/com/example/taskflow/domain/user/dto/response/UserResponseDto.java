package com.example.taskflow.domain.user.dto.response;

import com.example.taskflow.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public static UserResponseDto of(Long id, String username, String email, String name) {
        return new UserResponseDto(id, username, email, name);
    }

}
