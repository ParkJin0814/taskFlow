package com.example.taskflow.domain.common.converter;

import com.example.taskflow.domain.task.enums.TaskPriority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TaskPriorityConverter implements AttributeConverter<TaskPriority, Long> {
    @Override
    public Long convertToDatabaseColumn(TaskPriority priority) {
        return priority == null ? null : priority.getCode();
    }

    @Override
    public TaskPriority convertToEntityAttribute(Long dbData) {
        if (dbData == null) return null;
        return TaskPriority.toEnum(dbData); // static method 필요
    }
}
