
package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.domain.task.dto.request.TaskStatusUpdateRequestDto;
import com.example.taskflow.domain.task.dto.request.TaskUpdateRequestDto;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void 미리_세팅() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).name("test").build();
        task = Task.builder()
                .id(1L)
                .title("title")
                .description("desc")
                .priority(TaskPriority.MEDIUM)
                .assignedUser(user)
                .createdUser(user)
                .status(TaskStatus.TODO)
                .startLine(LocalDate.now())
                .deadLine(LocalDate.now().plusDays(3))
                .isDeleted(false)
                .build();
    }

    @Test
    void 태스크_생성_테스트() {
        TaskCreateRequestDto dto = new TaskCreateRequestDto("title", "desc", TaskPriority.MEDIUM, 1L, LocalDate.now(), LocalDate.now().plusDays(3),TaskStatus.TODO);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto response = taskService.createTask(dto);

        assertThat(response.getTitle()).isEqualTo("title");
    }

    @Test
    void 태스크_조회_테스트() {
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findAllByIsDeletedIsFalse(any(PageRequest.class))).thenReturn(page);

        Page<TaskResponseDto> result = taskService.searchTasks(PageRequest.of(0, 10), null);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void 상태로_태스크_조회_테스트() {
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findAllByStatusAndIsDeletedFalse(eq(TaskStatus.TODO), any(PageRequest.class))).thenReturn(page);

        Page<TaskResponseDto> result = taskService.searchTasks(PageRequest.of(0, 10), TaskStatus.TODO);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void 태스크_업데이트_테스트() {
        TaskUpdateRequestDto dto = new TaskUpdateRequestDto(TaskPriority.HIGH, 1L, LocalDate.now().plusDays(5));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        TaskResponseDto result = taskService.updateTask(1L, dto);

        assertThat(result.getPriority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void 태스크_삭제_예외처리() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        assertThat(task.getIsDeleted()).isTrue();
    }

    @Test
    void 태스크_삭제_이미삭제_예외처리() {
        task.deleteTask(LocalDateTime.now());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 삭제된 태스크입니다.");
    }

    @Test
    void 태스크_상태변경_에외처리() {
        TaskStatusUpdateRequestDto dto = new TaskStatusUpdateRequestDto(TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDto result = taskService.updateTaskStatus(dto, 1L);

        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void 태스크_상태변경_순차적으로_예외처리() {
        task.changeStatus(TaskStatus.IN_PROGRESS);
        TaskStatusUpdateRequestDto dto = new TaskStatusUpdateRequestDto(TaskStatus.TODO);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.updateTaskStatus(dto, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("상태는 순차적으로만");
    }

    @Test
    void 태스크_상태변경_예외처리() {
        TaskStatusUpdateRequestDto dto = new TaskStatusUpdateRequestDto(TaskStatus.DONE);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.updateTaskStatus(dto, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("유효하지 않은 상태 변경입니다.");
    }
}
