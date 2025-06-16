package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.domain.comment.dto.CommentRequestDto;
import com.example.taskflow.domain.comment.dto.CommentResponseDto;
import com.example.taskflow.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    Long authorId = 1L;
    Long taskId = 2L;

    // 댓글 생성
    @PostMapping("/tasks/{taskId}/comment")
    public CommentResponseDto createComment(
            @PathVariable Long taskId,
            @RequestBody CommentRequestDto requestDto
    ) {

        return commentService.createComment(taskId, authorId, requestDto);

    }

    // 특정 태스크의 댓글 조회
    @GetMapping("/tasks/{taskId}/comment")
    public List<CommentResponseDto> getCommentsByTask(@PathVariable Long taskId) {
        return commentService.getCommentsByTask(taskId);
    }

    // 댓글 검색 (내용에 대한 Like 검색, 키워드 검색?)



    // 댓글 수정
    @PutMapping("/comment/{commentId}")
    public void updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(commentId, requestDto);
    }

    // 댓글 삭제 (Soft Delete)
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }



}
