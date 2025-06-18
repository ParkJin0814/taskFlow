package com.example.taskflow.domain.comment.dto;

import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private Long taskId;
    private Long userId;
    private UserResponseDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTaskId().getId())
                .userId(comment.getAuthor().getId())
                .user(new UserResponseDto(comment.getAuthor()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}

