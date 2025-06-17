package com.example.taskflow.domain.activitylog.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class logListResponse {

    private final List<ActivitylogResponse> logs;

    private final int totalPages;

    private final int currentPage;

    public logListResponse(Page<ActivitylogResponse> list) {
        this.logs = list.getContent();
        this.totalPages = list.getTotalPages();
        this.currentPage = list.getNumber();
    }
}
