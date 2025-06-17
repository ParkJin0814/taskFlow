package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusUpdateRequestDto {
    @NotNull
    private TaskStatus status;

    public TaskStatusUpdateRequestDto(){

    }

    public TaskStatusUpdateRequestDto(TaskStatus taskStatus) {
        this.status = taskStatus;
    }
}
