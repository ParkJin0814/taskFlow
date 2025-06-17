package com.example.taskflow.domain.activitylog.dto.response;

import com.example.taskflow.domain.activitylog.entity.ActivityLog;
import com.example.taskflow.domain.activitylog.enums.ActivityType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivitylogResponse {
    private final Long id;
    private final LocalDateTime requestTime;
    private final Long userId;
    private final String ipAddress;
    private final String requestMethod;
    private final String requestUrl;
    private final ActivityType activityType;
    private final int targetId;

    public ActivitylogResponse(ActivityLog log) {
        this.id = log.getId();
        this.requestTime = log.getRequestTime();
        this.userId = log.getUserId().getId();
        this.ipAddress = log.getIpAddress();
        this.requestMethod = log.getRequestMethod();
        this.requestUrl = log.getRequestUrl();
        this.activityType = log.getActivityType();
        this.targetId = log.getTaskId();
    }
}
