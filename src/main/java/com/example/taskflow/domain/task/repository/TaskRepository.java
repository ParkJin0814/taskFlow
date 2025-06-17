package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import com.example.taskflow.domain.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.domain.Pageable;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Long countByStatusAndIsDeletedFalse(TaskStatus taskStatus);

    Long countByAssignedUserAndIsDeletedFalse(User assignedUser);

    Long countByAssignedUserAndStatusAndIsDeletedFalse(User userId, TaskStatus taskStatus);

    Long countByStatusInAndDeadLineBeforeAndIsDeletedFalse(List<TaskStatus> running, LocalDate now);

    @EntityGraph(attributePaths = {"assignedUser", "createdUser"})
    Page<Task> findByAssignedUserAndStatus(User assignedUser, TaskStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"assignedUser", "createdUser"})
    Page<Task> findByAssignedUser(User user, Pageable pageable);

    Long countByisDeletedFalse();
    @EntityGraph(attributePaths = {"assignedUser", "createdUser"})
    Page<Task> findAllByStatusAndIsDeletedFalse(TaskStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"assignedUser", "createdUser"})
    Page<Task> findAllByIsDeletedIsFalse(Pageable pageable);
}
