package com.example.taskflow.domain.task.controller;

import com.example.taskflow.global.config.aop.Logging;
import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.dto.PagedResponse;
import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.dto.request.TaskStatusUpdateRequestDto;
import com.example.taskflow.domain.task.dto.request.TaskUpdateRequestDto;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.service.TaskService;
import com.example.taskflow.global.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

/**
 * Task 관련 API 요청을 처리하는 컨트롤러입니다.
 * 태스크 생성, 조회(페이징), 수정, 삭제, 상태 업데이트 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * 새로운 태스크를 생성합니다.
     *
     * @param dto 생성에 필요한 태스크 정보
     * @return 생성된 태스크 정보를 담은 응답
     */
    @PostMapping
    @Logging
    public ResponseEntity<ApiResponse<TaskResponseDto>> createTask(
            @Valid @RequestBody TaskCreateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long userId = userDetails.getUserId();
        TaskResponseDto responseDto = taskService.createTask(dto, userId);
        return ResponseEntity.ok(ApiResponse.ok("태스크가 생성되었습니다.", responseDto));
    }

    /**
     * 상태에 따라 페이징된 태스크 목록을 조회합니다.
     *
     * @param pageable 페이징 정보 (page, size, sort 등)
     * @param status   필터링할 태스크 상태 (null일 경우 전체)
     * @return 페이징된 태스크 목록
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PagedResponse<TaskResponseDto>>> getTasks(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) TaskStatus status
    ) {
        Page<TaskResponseDto> taskResponseDtoList = taskService.searchTasks(pageable, status);
        PagedResponse<TaskResponseDto> pagedResponse = PagedResponse.from(taskResponseDtoList);
        return ResponseEntity.ok(ApiResponse.ok("태스크를 조회하였습니다.", pagedResponse));
    }

    /**
     * 태스크의 주요 정보를 수정합니다.
     *
     * @param id        수정할 태스크의 ID
     * @param updateDto 수정할 필드가 담긴 요청 객체
     * @return 수정된 태스크 정보
     */
    @PutMapping("/{id}")
    @Logging
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequestDto updateDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long userId = userDetails.getUserId();
        TaskResponseDto responseDto = taskService.updateTask(id, updateDto);
        return ResponseEntity.ok(ApiResponse.ok("태스크를 수정하였습니다.", responseDto));
    }

    /**
     * 태스크를 삭제(soft delete)합니다.
     *
     * @param id 삭제할 태스크의 ID
     * @return 삭제 성공 응답
     */
    @DeleteMapping("/{id}")
    @Logging
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long userId = userDetails.getUserId();
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.ok("태스크가 삭제되었습니다.", null));
    }

    /**
     * 태스크의 상태(status)를 업데이트합니다.
     *
     * @param requestDto 변경 요청 정보 (변경할 상태)
     * @param id         상태를 변경할 태스크의 ID
     * @return 상태가 변경된 태스크 정보
     */
    @PatchMapping("/{id}")
    @Logging
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTaskStatus(
            @Valid @RequestBody TaskStatusUpdateRequestDto requestDto,
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long userId = userDetails.getUserId();
        TaskResponseDto responseDto = taskService.updateTaskStatus(requestDto, id);
        return ResponseEntity.ok(ApiResponse.ok("태스크를 수정하였습니다.", responseDto));
    }
}
