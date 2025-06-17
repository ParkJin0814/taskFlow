package com.example.taskflow.domain.task.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TaskPriority {
    LOW(1L),
    MEDIUM(2L),
    HIGH(3L);

    private final Long code;

    TaskPriority(Long code) {
        this.code = code;
    }

    public static TaskPriority toEnum(Long code) {
        return Arrays.stream(TaskPriority.values())
                .filter(p -> p.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }
}
