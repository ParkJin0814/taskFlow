package com.example.taskflow.domain.comment.service;

import com.example.taskflow.domain.comment.dto.CommentRequestDto;
import com.example.taskflow.domain.comment.dto.CommentResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    //댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long taskId, Long authorId, CommentRequestDto requestDto) {

        // taskId에 맞는 Task 가져오기
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Task 없음"));

        // authorId에 맞는 User 가져오기
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User 없음"));

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .author(author)
                .taskId(task)
                .build();

        commentRepository.save(comment);
        return toDto(comment);
    }


    // 특정 task의 댓글 조회 (삭제 안된 것만 조회, 최신순)
    public List<CommentResponseDto> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));

        List<Comment> commentList = commentRepository.findByTaskIdAndIsDeletedFalseOrderByCreatedAtDesc(task);
        return commentList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());  //스트림 내 결과 모아서 리스트responsedto로
    }


    // 댓글 내용 검색 (삭제 안된 것만, 키워드 검색?)



    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Comment가 존재하지 않습니다."));

        comment.setContent(requestDto.getContent());
    }


    // 댓글 삭제 (Soft Delete)
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Comment가 존재하지 않습니다."));

        comment.softDelete();
    }


    // 공통 DTO 변환 메서드
    private CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getName())  // 작성자 이름 출력
                .taskId(comment.getTaskId().getId())      // task의 id 출력
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }





}
