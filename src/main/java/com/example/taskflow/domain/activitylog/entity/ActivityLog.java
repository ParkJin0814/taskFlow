package com.example.taskflow.domain.activitylog.entity;


import com.example.taskflow.domain.activitylog.enums.ActivityType;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_log")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime requestTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    private String ipAddress;

    private String requestMethod;

    private String requestUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    private Integer taskId;

    public ActivityLog(LocalDateTime now, User userId, String ipAddress, String requestMethod, String requestUrl, ActivityType activityType, int taskId) {
        this.requestTime = now;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.activityType = activityType;
        this.taskId = taskId;
    }

    @PrePersist
    public void onCreate() {
        if (requestTime == null) {
            requestTime = LocalDateTime.now();
        }
    }
}

