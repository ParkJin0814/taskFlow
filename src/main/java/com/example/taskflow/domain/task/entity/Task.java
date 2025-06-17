package com.example.taskflow.domain.task.entity;

import com.example.taskflow.domain.common.converter.TaskPriorityConverter;
import com.example.taskflow.domain.common.entity.BaseEntity;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 업무(Task) 엔티티 클래스입니다.
 * 업무의 제목, 설명, 중요도, 담당자, 생성자, 시작일, 마감일, 상태, 삭제 여부 등의 정보를 담고 있습니다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@Builder
public class Task extends BaseEntity {

    /** 업무 ID (기본 키) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 업무 제목 (필수) */
    @Column(nullable = false)
    private String title;

    /** 업무 설명 (선택) */
    private String description;

    /** 업무 중요도 (필수) - ENUM(String으로 저장) */
    @Convert(converter = TaskPriorityConverter.class)
    private TaskPriority priority;


    /** 업무 담당자 (필수) - User와 다대일 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id", nullable = false)
    private User assignedUser;

    /** 업무 생성자 (필수) - User와 다대일 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user_id", nullable = false)
    private User createdUser;

    /** 업무 시작일 */
    private LocalDate startLine;

    /** 업무 마감일 */
    private LocalDate deadLine;

    /** 업무 상태 (필수) - TODO / IN_PROGRESS / DONE 등 ENUM */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    /**
     * 업무 수정 (중요도, 담당자, 마감일 변경)
     * @param priority 변경할 중요도
     * @param assignedUser 변경할 담당자
     * @param deadLine 변경할 마감일
     */
    public void updateTask(TaskPriority priority, User assignedUser, LocalDate deadLine) {
        this.priority = priority;
        this.assignedUser = assignedUser;
        this.deadLine = deadLine;
    }

    /**
     * 업무 상태 변경
     * @param status 새로운 상태
     */
    public void changeStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * 영속화(Persist) 전 디폴트 값 초기화
     * - 상태가 null이면 기본값 TODO 설정
     * - isDeleted가 null이면 false로 초기화
     */
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = TaskStatus.TODO;
        }
    }
}
