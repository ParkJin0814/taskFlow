package com.example.taskflow.domain.dashboard.dto.response;

import lombok.Getter;

@Getter
public class DashboardOverResponse {
    private Long taskOver;

    public DashboardOverResponse(Long taskOver) {
        this.taskOver = taskOver;
    }
}
