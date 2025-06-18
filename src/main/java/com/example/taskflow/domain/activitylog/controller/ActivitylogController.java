package com.example.taskflow.domain.activitylog.controller;

import com.example.taskflow.domain.activitylog.dto.response.ActivitylogResponse;
import com.example.taskflow.domain.activitylog.enums.ActivityType;
import com.example.taskflow.domain.activitylog.service.ActivitylogService;
import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActivitylogController {

    private final ActivitylogService activitylogService;

    @GetMapping("/activities/my")
    public ResponseEntity<ApiResponse<PagedResponse<ActivitylogResponse>>> logSearch(@RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "10") int size,
                                                                                    @RequestParam(required = false) Long userId,
                                                                                    @RequestParam(required = false) ActivityType activityType,
                                                                                    @RequestParam(required = false) Integer targetId,
                                                                                    @RequestParam(defaultValue = "requestTime") String sortBy,
                                                                                    @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(direction), sortBy)
        );
        Page<ActivitylogResponse> list = activitylogService.logSearch(userId, activityType, targetId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("활동 로그가 조회 되었습니다.",PagedResponse.from(list)));
    }
}
