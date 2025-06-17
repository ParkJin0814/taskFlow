package com.example.taskflow.domain.dashboard.service;

import com.example.taskflow.domain.dashboard.dto.response.DashboardMainResponse;
import com.example.taskflow.domain.dashboard.dto.response.DashboardOverResponse;
import com.example.taskflow.domain.dashboard.dto.response.DashboardRateResponse;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public DashboardMainResponse dashboardMain() {
        Long taskTotal = totalTasks();
        Long taskTodo = todoTaskStatus(TaskStatus.TODO);
        Long taskProgress = todoTaskStatus(TaskStatus.IN_PROGRESS);
        Long taskDone = todoTaskStatus(TaskStatus.DONE);
        return new DashboardMainResponse(taskTotal, taskTodo, taskProgress, taskDone);
    }

    public DashboardRateResponse dashboardRate(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Long taskTotal = totalTasks();
        Long taskTodo = todoTaskStatus(TaskStatus.TODO);
        Long taskProgress = todoTaskStatus(TaskStatus.IN_PROGRESS);
        Long taskDone = todoTaskStatus(TaskStatus.DONE);
        Long myDone = myDone(user);
        Long myTaskTotal = myTask(user);

        double successTeamRate = rate(taskDone , taskTotal);

        double successMyRate = rate(myDone , myTaskTotal);
        return new DashboardRateResponse(taskTodo, taskProgress, taskDone, successTeamRate, successMyRate);
    }

    public Page<TaskResponseDto> dashboardMyTask(Long userId, TaskStatus taskStatus, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow();
        if(taskStatus != null) {
            return taskRepository.findByAssignedUserAndStatus(user, taskStatus, pageable).map(TaskResponseDto::toDto);
        }
        return taskRepository.findByAssignedUser(user , pageable).map(TaskResponseDto::toDto);
    }

    public DashboardOverResponse dashboardOver() {
        List<TaskStatus> running = List.of(TaskStatus.TODO, TaskStatus.IN_PROGRESS);
        return new DashboardOverResponse(taskRepository.countByStatusInAndDeadLineBeforeAndIsDeletedFalse(running, LocalDate.now()));
    }

    private Long totalTasks() {
        return taskRepository.countByisDeletedFalse();
    }

    private Long todoTaskStatus(TaskStatus taskStatus) {
        return taskRepository.countByStatusAndIsDeletedFalse(taskStatus);
    }

    private Long myDone(User userId) {
        return taskRepository.countByAssignedUserAndStatusAndIsDeletedFalse(userId, TaskStatus.DONE);
    }

    private Long myTask(User userId) {
        return taskRepository.countByAssignedUserAndIsDeletedFalse(userId);
    }

    private Double rate(Long my, Long total) {
        if(total == 0) {
            return 0.0;
        }
        String rateCul = String.format("%.2f", (double) my / total * 100);
        return Double.parseDouble(rateCul);
    }
}
