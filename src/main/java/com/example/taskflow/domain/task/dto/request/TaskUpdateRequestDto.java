package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskPriority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TaskUpdateRequestDto {

    @NotNull
    private TaskPriority priority;

    @NotNull
    private Long assignedUserId;

    @NotNull
    private LocalDate deadLine;
}
