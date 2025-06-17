package com.example.taskflow.domain.activitylog.service;

import com.example.taskflow.domain.activitylog.dto.response.ActivitylogResponse;
import com.example.taskflow.domain.activitylog.entity.ActivityLog;
import com.example.taskflow.domain.activitylog.enums.ActivityType;
import com.example.taskflow.domain.activitylog.repository.ActivitylogRepository;
import com.example.taskflow.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
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


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivitylogServiceTest {

    @Mock
    private ActivitylogRepository activitylogRepository;

    @InjectMocks
    private ActivitylogService activitylogService;

    private Pageable pageable;
    private User user1;
    private User user2;
    private ActivityLog activityLog1;
    private ActivityLog activityLog2;
    private Page<ActivityLog> activityLogsPage;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        user1 = new User();
        ReflectionTestUtils.setField(user1, "id", 1L);
        user2 = new User();
        ReflectionTestUtils.setField(user2, "id", 1L);

        activityLog1 = new ActivityLog(1L, LocalDateTime.now(), user1, "ip1", "GET", "/test", ActivityType.USER_LOGGED_IN, 3);
        activityLog2 = new ActivityLog(2L, LocalDateTime.now(), user2, "ip2", "GET", "/test", ActivityType.TASK_CREATED, 1);

    }

    @Test
    @DisplayName("유저 id를 통한 로그 조회 테스트")
    void logsearchwithuserId() {
        //given
        Long userId = user1.getId();
        activityLogsPage = new PageImpl<>(List.of(activityLog1));
        when(activitylogRepository.findByUserId_Id(userId, pageable)).thenReturn(activityLogsPage);

        //when
        Page<ActivitylogResponse> result = activitylogService.logSearch(userId, null, null, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(activitylogRepository).findByUserId_Id(userId, pageable);
    }

    @Test
    @DisplayName("활동 타입과 타겟 아이디를 통한 로그 조회 테스트")
    void logsearchwithActivityTypeAndTaskId() {
        //given
        ActivityType activityType = activityLog1.getActivityType();
        Integer taskId = activityLog1.getTaskId();
        activityLogsPage = new PageImpl<>(List.of(activityLog1));
        when(activitylogRepository.findByActivityTypeAndTaskId(activityType, taskId, pageable)).thenReturn(activityLogsPage);

        //when
        Page<ActivitylogResponse> result = activitylogService.logSearch(null, activityType, taskId, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(activitylogRepository).findByActivityTypeAndTaskId(activityType, taskId, pageable);
    }

    @Test
    @DisplayName("활동 타입을 통한 로그 조회 테스트")
    void logsearchwithActivityType() {
        //given
        ActivityType activityType = activityLog1.getActivityType();
        activityLogsPage = new PageImpl<>(List.of(activityLog1));
        when(activitylogRepository.findByActivityType(activityType, pageable)).thenReturn(activityLogsPage);

        //when
        Page<ActivitylogResponse> result = activitylogService.logSearch(null, activityType, null, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(activitylogRepository).findByActivityType(activityType, pageable);
    }

    @Test
    @DisplayName("타겟 아이디를 통한 로그 조회 테스트")
    void logsearchwithTaskId() {
        //given
        Integer taskId = activityLog1.getTaskId();
        activityLogsPage = new PageImpl<>(List.of(activityLog1));
        when(activitylogRepository.findByTaskId(taskId, pageable)).thenReturn(activityLogsPage);

        //when
        Page<ActivitylogResponse> result = activitylogService.logSearch(null, null, taskId, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(activitylogRepository).findByTaskId(taskId, pageable);
    }

    @Test
    @DisplayName("아무 입력값이 들어오지 않은 로그 조회 테스트")
    void logsearch() {
        //given
        activityLogsPage = new PageImpl<>(List.of(activityLog1, activityLog2));
        when(activitylogRepository.findAll(pageable)).thenReturn(activityLogsPage);

        //when
        Page<ActivitylogResponse> result = activitylogService.logSearch(null, null, null, pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(activitylogRepository).findAll(pageable);
    }
}