package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.domain.comment.dto.CommentRequestDto;
import com.example.taskflow.domain.comment.dto.CommentResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.service.CommentService;
import com.example.taskflow.domain.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    Long authorId = 1L;  //임시


    // 댓글 생성
    @PostMapping("/tasks/{taskId}/comment")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable Long taskId,
            @RequestBody @Valid CommentRequestDto requestDto) {

        CommentResponseDto responseDto = commentService.createComment(taskId, authorId, requestDto);
        return ResponseEntity.ok(ApiResponse.ok("댓글이 생성되었습니다.", responseDto));

    }

    // 특정 태스크의 댓글 조회
    @GetMapping("/tasks/{taskId}/comment")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getCommentsByTask(
            @PathVariable Long taskId) {

        List<CommentResponseDto> responseList = commentService.getCommentsByTask(taskId);
        return ResponseEntity.ok(ApiResponse.ok("테스크의 댓글을 조회하였습니다.", responseList));
    }

    // 댓글 검색 (내용에 대한 Like 검색, 키워드 검색?)



    // 댓글 수정
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequestDto requestDto) {

        Comment updatedComment = commentService.updateComment(commentId, requestDto);
        CommentResponseDto responseDto = CommentResponseDto.toDto(updatedComment);
        return ResponseEntity.ok(ApiResponse.ok("댓글을 수정하였습니다.", responseDto));
    }

    // 댓글 삭제 (Soft Delete)
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글을 삭제하였습니다.", null));
    }



}
