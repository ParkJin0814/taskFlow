package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 업무(Task) 생성을 위한 요청 DTO입니다.
 * 클라이언트로부터 전달받은 업무 생성 데이터를 캡슐화합니다.
 *
 * @param title        업무 제목 (필수)
 * @param description  업무 설명 (선택)
 * @param priority     업무 중요도 (필수) - LOW, MEDIUM, HIGH 등의 enum 값
 * @param assignedId   담당자 사용자 ID (선택이나 일반적으로 필수로 처리)
 * @param dueDate     업무 마감일 (선택)
 * @param status       업무 상태 (필수) - TODO, IN_PROGRESS, DONE 등의 enum 값
 */
public record TaskCreateRequestDto(
        @NotNull String title,
        String description,
        @NotNull TaskPriority priority,
        Long assignedId,
        LocalDate dueDate,
        TaskStatus status
) {

}
