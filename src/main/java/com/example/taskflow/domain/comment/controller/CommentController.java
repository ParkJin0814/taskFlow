package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.domain.common.dto.PagedResponse;
import com.example.taskflow.global.config.aop.Logging;
import com.example.taskflow.domain.comment.dto.CommentRequestDto;
import com.example.taskflow.domain.comment.dto.CommentResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.service.CommentService;
import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.global.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    // 댓글 생성
    @PostMapping("/api/tasks/{taskId}/comments")
    @Logging
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable Long taskId,
            @RequestBody @Valid CommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        Long userId = userDetails.getUserId();

        CommentResponseDto responseDto = commentService.createComment(taskId, userId, requestDto);
        return ResponseEntity.ok(ApiResponse.ok("댓글이 생성되었습니다.", responseDto));

    }

    // 특정 태스크의 댓글 조회
    @GetMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<PagedResponse<CommentResponseDto>>> getCommentsByTask(
            @PathVariable Long taskId,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponseDto> commentPage = commentService.getCommentsByTask(taskId, pageable);

        PagedResponse<CommentResponseDto> pagedResponse = PagedResponse.from(commentPage);

        return ResponseEntity.ok(ApiResponse.ok("댓글 목록을 조회했습니다.", pagedResponse));
    }

    // 댓글 검색 (특정 게시물의 댓글 중 Like 검색, 키워드 검색)
    @GetMapping("/api/tasks/{taskId}/comments/search")
    public ResponseEntity<ApiResponse<PagedResponse<CommentResponseDto>>> searchComments(
            @RequestParam String keyword,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponseDto> commentPage = commentService.searchComments(keyword, pageable);
        PagedResponse<CommentResponseDto> pagedResponse = PagedResponse.from(commentPage);

        return ResponseEntity.ok(ApiResponse.ok("키워드가 포함된 댓글이 검색되었습니다.", pagedResponse));
    }

    // 댓글 수정
    @PutMapping("/api/comments/{commentId}")
    @Logging
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequestDto requestDto) {

        Comment updatedComment = commentService.updateComment(commentId, requestDto);
        CommentResponseDto responseDto = CommentResponseDto.toDto(updatedComment);
        return ResponseEntity.ok(ApiResponse.ok("댓글을 수정하였습니다.", responseDto));
    }

    // 댓글 삭제 (Soft Delete)
    @DeleteMapping("/api/comments/{commentId}")
    @Logging
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글을 삭제하였습니다.", null));
    }



}
