package com.example.taskflow.domain.comment.repository;

import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.task.entity.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 Task의 댓글들을 최신순으로 조회 (삭제안된 것만, 최신순 정렬)
    @EntityGraph(attributePaths = {"author", "taskId"})
    List<Comment> findByTaskIdAndIsDeletedFalseOrderByCreatedAtDesc(Task task);

    // 댓글 내용에 keyword가 포함된 것 검색 (Soft Delete 제외)
    @EntityGraph(attributePaths = {"author", "taskId"})
    List<Comment> findByContentContainingAndIsDeletedFalse(String keyword);
}
