package com.example.taskflow.domain.user.entity;

import com.example.taskflow.domain.common.entity.BaseEntity;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users") // 테이블명은 "users"
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @PrePersist
    public void prePersist() {
        if (role == null) {
            role = UserRole.USER;
        }
    }

    public User (String userName, String email, String password, String name) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
