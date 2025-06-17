package com.example.taskflow.domain.dashboard.dto.response;

import lombok.Getter;

@Getter
public class DashboardMainResponse {
    private final Long taskTotal;
    private final Long taskTodo;
    private final Long taskProgress;
    private final Long taskDone;


    public DashboardMainResponse(Long taskTotal, Long taskTodo, Long taskProgress, Long taskDone) {
        this.taskTotal = taskTotal;
        this.taskTodo = taskTodo;
        this.taskProgress = taskProgress;
        this.taskDone = taskDone;
    }
}
