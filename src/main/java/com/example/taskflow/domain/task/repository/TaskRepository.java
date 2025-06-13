package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
