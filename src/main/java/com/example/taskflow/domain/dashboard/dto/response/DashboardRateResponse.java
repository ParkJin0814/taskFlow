package com.example.taskflow.domain.dashboard.dto.response;

import lombok.Getter;

@Getter
public class DashboardRateResponse {
    private final Long taskTodo;
    private final Long taskProgress;
    private final Long taskDone;
    private final double successTeamRate;
    private final double successMyRate;

    public DashboardRateResponse(Long taskTodo, Long taskProgress, Long taskDone, double successTeamRate, double successMyRate) {
        this.taskTodo = taskTodo;
        this.taskProgress = taskProgress;
        this.taskDone = taskDone;
        this.successTeamRate = successTeamRate;
        this.successMyRate = successMyRate;
    }
}
