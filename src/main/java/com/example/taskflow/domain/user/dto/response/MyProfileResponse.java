package com.example.taskflow.domain.user.dto.response;

import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyProfileResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final UserRole role;
    private final LocalDateTime createdAt;

    public MyProfileResponse(Long id, String username, String email, String name, UserRole role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static MyProfileResponse toDto(User user){
        return new MyProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

}
