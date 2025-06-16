package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskPriority;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskUpdateRequestDto {

    @NotNull
    private TaskPriority priority;

    @NotNull
    private Long assignedUserId;

    @NotNull
    private LocalDate deadLine;
}
