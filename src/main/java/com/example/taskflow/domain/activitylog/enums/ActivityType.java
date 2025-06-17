package com.example.taskflow.domain.activitylog.enums;

import lombok.Getter;

import java.util.Arrays;

public enum ActivityType {
    TASK_CREATED("POST/tasks",1),
    TASK_UPDATED("PUT/tasks/{id}",1),
    TASK_DELETED("DELETE/tasks/{id}",1),
    TASK_STATUS_CHANGED("PATCH/tasks/{id}/status",1),
    COMMENT_CREATED("POST/tasks/{taskId}/comment",2),
    COMMENT_UPDATED("PUT/comment/{commentId}",2),
    COMMENT_DELETED("DELETE/comment/{commentId}",2),
    USER_LOGGED_IN("POST/login",3),
    USER_LOGGED_OUT("POST/logout",3);

    private final String requestUrl;
    @Getter
    private final int taskId;
    ActivityType(String requestUrl, int taskId) {
        this.requestUrl = requestUrl;
        this.taskId = taskId;
    }

    public static ActivityType type(String url) {
        return Arrays.stream(values())
                .filter(value -> match(url, value.requestUrl))
                .findFirst()
                .orElse(null);
    }

    private static boolean match(String url, String requestUrl) {
        // 경로 변수({id}, {taskId} 등)를 정규식으로 변환
        String regex = requestUrl.replaceAll("\\{[^/]+}", "[^/]+");
        return url.matches(regex);
    }

}
