package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TaskStatusUpdateRequestDto {
    @NotNull
    private TaskStatus status;
}
