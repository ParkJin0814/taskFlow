package com.example.taskflow.domain.task.dto.response;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TaskResponseDto {
    private Long id;

    private String title;

    private String description;

    private TaskPriority priority;

    private String assignedUserName;

    private String createdUserName;

    private LocalDate startLine;

    private LocalDate deadLine;

    private TaskStatus status;

    private LocalDateTime createdAt;

    public static TaskResponseDto toDto(Task task){
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getAssignedUser().getName(),
                task.getCreatedUser().getName(),
                task.getStartLine(),
                task.getDeadLine(),
                task.getStatus(),
                task.getCreatedAt());
    }
}
