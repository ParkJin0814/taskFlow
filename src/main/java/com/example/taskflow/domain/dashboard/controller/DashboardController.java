package com.example.taskflow.domain.dashboard.controller;

import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.dto.PagedResponse;
import com.example.taskflow.domain.dashboard.dto.response.DashboardMainResponse;
import com.example.taskflow.domain.dashboard.dto.response.DashboardOverResponse;
import com.example.taskflow.domain.dashboard.dto.response.DashboardRateResponse;
import com.example.taskflow.domain.dashboard.service.DashboardService;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardMainResponse>> dashboardMain() {
        return ResponseEntity.ok(ApiResponse.ok("대시보드의 조회가 완료되었습니다." , dashboardService.dashboardMain()));
    }

    @GetMapping("/dashboard/rate")
    public ResponseEntity<ApiResponse<DashboardRateResponse>> dashboardRate() {
        return ResponseEntity.ok(ApiResponse.ok("태스크 통계 조회가 완료되었습니다.",dashboardService.dashboardRate(1L)));
    }

    @GetMapping("/dashboard/over")
    public ResponseEntity<ApiResponse<DashboardOverResponse>> dashboardOver() {
        return ResponseEntity.ok(ApiResponse.ok("기간이 지난 태스크 수의 조회가 완료되었습니다." , dashboardService.dashboardOver()));
    }

    @GetMapping("/dashboard/mytask")
    public ResponseEntity<ApiResponse<PagedResponse<TaskResponseDto>>> dashboardMyTask(@RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "10") int size,
                                                                                       @RequestParam(required = false) TaskStatus taskStatus) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"priority"));

        return ResponseEntity.ok(ApiResponse.ok("내 태스크 조회가 완료되었습니다.",PagedResponse.from(dashboardService.dashboardMyTask(2L, taskStatus, pageable))));
    }
}
