package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByStatusAndIsDeletedFalse(TaskStatus status, Pageable pageable);

    Page<Task> findAllByIsDeletedIsFalse(Pageable pageable);
}
