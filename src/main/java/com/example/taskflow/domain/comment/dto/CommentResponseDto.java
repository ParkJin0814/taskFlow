package com.example.taskflow.domain.comment.dto;

import com.example.taskflow.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private String author;
    private Long taskId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getName())
                .taskId(comment.getTaskId().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}

