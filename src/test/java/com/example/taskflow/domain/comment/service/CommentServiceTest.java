package com.example.taskflow.domain.comment.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.taskflow.domain.comment.dto.CommentRequestDto;
import com.example.taskflow.domain.comment.dto.CommentResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.common.exception.BaseException;
import com.example.taskflow.domain.common.exception.ErrorCode;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class CommentServiceTest {


    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void 댓글_생성_성공() {
        Long taskId = 1L;
        Long authorId = 2L;
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("new comment");

        Task task = mock(Task.class);
        User author = mock(User.class);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArgument(0));

        CommentResponseDto response = commentService.createComment(taskId, authorId, requestDto);

        assertNotNull(response);
        assertEquals("new comment", response.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }



    // 댓글 삭제 시 softDelete() 메서드가 호출되어 isDeleted 플래그가 변경되는지 테스트
    @Test
    void 댓글_삭제_테스트() {
        Long commentId = 1L;
        Comment mockComment = mock(Comment.class);

        // commentRepository.findById()가 Optional.of(mockComment) 반환하도록 설정
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        commentService.deleteComment(commentId);

        // comment 객체의 softDelete() 메서드가 호출됐는지 검증
        verify(mockComment, times(1)).softDelete();
    }

}