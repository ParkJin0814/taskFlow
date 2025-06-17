package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.domain.task.dto.request.TaskStatusUpdateRequestDto;
import com.example.taskflow.domain.task.dto.request.TaskUpdateRequestDto;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.taskflow.domain.task.dto.response.TaskResponseDto.toDto;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * 새로운 Task(업무)를 생성합니다.
     *
     * @param dto Task 생성 요청 정보를 담은 DTO (제목, 설명, 중요도, 담당자 ID, 상태, 시작일, 마감일 포함)
     * @return 생성된 Task의 정보를 담은 DTO
     */
    public TaskResponseDto createTask(TaskCreateRequestDto dto) {
        User assignedUser = userRepository.findById(dto.assignedId())
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        User createdUser = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));

        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .assignedUser(assignedUser)
                .createdUser(createdUser)
                .status(dto.status())
                .startLine(dto.startLine())
                .deadLine(dto.deadLine())
                .build();

        taskRepository.save(task);
        return toDto(task);
    }

    /**
     * Task 목록을 조회합니다. 상태가 null인 경우 전체 비삭제 Task를 조회하고,
     * 특정 상태가 주어지면 해당 상태의 Task만 조회합니다.
     *
     * @param pageable 페이징 정보
     * @param status   조회할 Task 상태 (null 허용)
     * @return Task 목록의 Page 객체 (DTO 형태)
     */
    public Page<TaskResponseDto> searchTasks(Pageable pageable, TaskStatus status) {
        if (status == null) {
            return taskRepository.findAllByIsDeletedIsFalse(pageable)
                    .map(TaskResponseDto::toDto);
        } else {
            return taskRepository.findAllByStatusAndIsDeletedFalse(status, pageable)
                    .map(TaskResponseDto::toDto);
        }
    }

    /**
     * 특정 Task를 수정합니다.
     *
     * @param id        수정할 Task의 ID
     * @param updateDto 수정 요청 정보 (중요도, 담당자 ID, 마감일 포함)
     * @return 수정된 Task의 DTO
     */
    @Transactional
    public TaskResponseDto updateTask(Long id, TaskUpdateRequestDto updateDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("태스크가 없습니다."));

        Long assigneeId = updateDto.getAssignedUserId();

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));

        task.updateTask(updateDto.getPriority(), assignee, updateDto.getDeadLine());
        return toDto(task);
    }

    /**
     * 특정 Task를 논리 삭제 처리합니다.
     *
     * @param id 삭제할 Task의 ID
     */
    @Transactional
    public void deleteTask(Long id) {
        LocalDateTime now = LocalDateTime.now();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("태스크가 없습니다."));

        if (task.getIsDeleted()) {
            throw new RuntimeException("이미 삭제된 태스크입니다.");
        }

        task.deleteTask(now);
    }

    /**
     * Task의 상태를 업데이트합니다.
     * 상태 변경은 TODO → IN_PROGRESS → DONE 순서만 허용합니다.
     *
     * @param requestDto 상태 변경 요청 DTO
     * @param id         대상 Task의 ID
     * @return 상태가 변경된 Task의 DTO
     */
    @Transactional
    public TaskResponseDto updateTaskStatus(TaskStatusUpdateRequestDto requestDto, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("태스크가 없습니다."));

        TaskStatus currentStatus = task.getStatus();
        TaskStatus requestedStatus = requestDto.getStatus();

        if (requestedStatus.ordinal() <= currentStatus.ordinal()) {
            throw new RuntimeException("상태는 순차적으로만 변경할 수 있으며, 이전 단계로 되돌릴 수 없습니다.");
        }

        if ((currentStatus == TaskStatus.TODO && requestedStatus == TaskStatus.IN_PROGRESS) ||
                (currentStatus == TaskStatus.IN_PROGRESS && requestedStatus == TaskStatus.DONE)) {
            task.changeStatus(requestedStatus);
            return toDto(task);
        } else {
            throw new RuntimeException("유효하지 않은 상태 변경입니다.");
        }
    }
}
