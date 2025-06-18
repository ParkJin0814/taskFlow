package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TaskUpdateRequestDto {

    private String title;

    private String description;

    private LocalDate dueDate;

    @NotNull
    private TaskPriority priority;

    private TaskStatus status;

    @NotNull
    private Long assigneeId;



}
