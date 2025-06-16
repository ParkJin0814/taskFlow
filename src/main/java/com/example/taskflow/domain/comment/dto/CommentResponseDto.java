package com.example.taskflow.domain.comment.dto;

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

}

