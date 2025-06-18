package com.example.taskflow.domain.dashboard.service;

import com.example.taskflow.domain.dashboard.dto.response.DashboardMainResponse;
import com.example.taskflow.domain.dashboard.dto.response.DashboardOverResponse;
import com.example.taskflow.domain.dashboard.dto.response.DashboardRateResponse;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    @DisplayName("대시보드 메인 출력 테스트")
    void dashboardMain() {
        //given
        when(taskRepository.countByisDeletedFalse()).thenReturn(10L);
        when(taskRepository.countByStatusAndIsDeletedFalse(TaskStatus.TODO)).thenReturn(5L);
        when(taskRepository.countByStatusAndIsDeletedFalse(TaskStatus.IN_PROGRESS)).thenReturn(3L);
        when(taskRepository.countByStatusAndIsDeletedFalse(TaskStatus.DONE)).thenReturn(2L);
        //when
        DashboardMainResponse result = dashboardService.dashboardMain();
        //then
        assertThat(result.getTaskTotal()).isEqualTo(10L);
        assertThat(result.getTaskTodo()).isEqualTo(5L);
        assertThat(result.getTaskProgress()).isEqualTo(3L);
        assertThat(result.getTaskDone()).isEqualTo(2L);
    }

    @Test
    @DisplayName("태스크 통계가 출력되는지 테스트")
    void dashboardRate() {
        //given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.countByisDeletedFalse()).thenReturn(3L);
        when(taskRepository.countByStatusAndIsDeletedFalse(TaskStatus.TODO)).thenReturn(1L);
        when(taskRepository.countByStatusAndIsDeletedFalse(TaskStatus.IN_PROGRESS)).thenReturn(1L);
        when(taskRepository.countByStatusAndIsDeletedFalse(TaskStatus.DONE)).thenReturn(1L);
        when(taskRepository.countByAssignedUserAndStatusAndIsDeletedFalse(user, TaskStatus.DONE)).thenReturn(0L);
        when(taskRepository.countByAssignedUserAndIsDeletedFalse(user)).thenReturn(0L);

        //when
        DashboardRateResponse result = dashboardService.dashboardRate(1L);

        //then
        assertThat(result.getSuccessTeamRate()).isEqualTo(33.33);
        assertThat(result.getSuccessMyRate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("대시보드에 내 테스크 목록 출력 테스트")
    void dashboardMyTask() {
        //given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        Pageable pageable = PageRequest.of(0, 10);
        Task task = Task.builder()
                .id(1L)
                .title("title")
                .description("desc")
                .priority(TaskPriority.MEDIUM)
                .assignedUser(user)
                .status(TaskStatus.TODO)
                .dueDate(LocalDate.now().plusDays(3))
                .build();
        ReflectionTestUtils.setField(task, "id", 1L);
        PageImpl<Task> tasks = new PageImpl<>(List.of(task));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findByAssignedUserAndIsDeletedFalse(user , pageable)).thenReturn(tasks);
        //when
        Page<TaskResponseDto> result = dashboardService.dashboardMyTask(user.getId(), null, pageable);
        //then
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        verify(taskRepository).findByAssignedUserAndIsDeletedFalse(user , pageable);
    }


    @Test
    @DisplayName("태스크 상태에 해당하는 내 테스크 목록 확인 테스트")
    void dashboardMyTaskWithTaskStatus() {
        //given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        Pageable pageable = PageRequest.of(0, 10);
        Task task = Task.builder()
                .id(1L)
                .title("title")
                .description("desc")
                .priority(TaskPriority.MEDIUM)
                .assignedUser(user)
                .status(TaskStatus.TODO)
                .dueDate(LocalDate.now().plusDays(3))
                .build();
        ReflectionTestUtils.setField(task, "id", 1L);
        PageImpl<Task> tasks = new PageImpl<>(List.of(task));
        TaskStatus taskStatus = TaskStatus.TODO;
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findByAssignedUserAndStatusAndIsDeletedFalse(user, taskStatus, pageable)).thenReturn(tasks);
        //when
        Page<TaskResponseDto> result = dashboardService.dashboardMyTask(user.getId(), taskStatus, pageable);
        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        verify(taskRepository).findByAssignedUserAndStatusAndIsDeletedFalse(user, taskStatus, pageable);

    }

    @Test
    @DisplayName("마감기한이 지난 태스크의 수 출력 테스트")
    void dashboardOver() {
        //given
        List<TaskStatus> running = List.of(TaskStatus.TODO, TaskStatus.IN_PROGRESS);
        when(taskRepository.countByStatusInAndDueDateBeforeAndIsDeletedFalse(running, LocalDate.now())).thenReturn(1L);

        //when
        DashboardOverResponse result = dashboardService.dashboardOver();
        //then
        assertThat(result.getTaskOver()).isEqualTo(1);
        verify(taskRepository).countByStatusInAndDueDateBeforeAndIsDeletedFalse(running, LocalDate.now());

    }
}