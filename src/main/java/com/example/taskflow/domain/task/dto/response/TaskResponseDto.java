package com.example.taskflow.domain.task.dto.response;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.user.dto.response.UserResponseDto;
import com.example.taskflow.domain.user.entity.User;
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

    private TaskStatus status;

    private Long assignedId;

    private UserResponseDto assignee;

    private LocalDate dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static TaskResponseDto toDto(Task task){
        User AssignedUser = task.getAssignedUser();
        UserResponseDto userResponseDto = new UserResponseDto(AssignedUser);
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getAssignedUser().getId(),
                userResponseDto,
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
