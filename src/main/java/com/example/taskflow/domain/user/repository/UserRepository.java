package com.example.taskflow.domain.user.repository;

import com.example.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
