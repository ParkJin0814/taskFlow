package com.example.taskflow.domain.activitylog.service;

import com.example.taskflow.domain.activitylog.dto.response.ActivitylogResponse;
import com.example.taskflow.domain.activitylog.enums.ActivityType;
import com.example.taskflow.domain.activitylog.repository.ActivitylogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivitylogService {

    private final ActivitylogRepository activitylogRepository;

    public Page<ActivitylogResponse> logSearch(Long userId, ActivityType activityType, Integer targetId, Pageable pageable) {

        if(userId != null) {
            return activitylogRepository.findByUserId_Id(userId, pageable).map(ActivitylogResponse::new);
        }
        else if(activityType != null && targetId != null) {
            return activitylogRepository.findByActivityTypeAndTaskId(activityType, targetId, pageable).map(ActivitylogResponse::new);
        }
        else if(activityType != null) {
            return activitylogRepository.findByActivityType(activityType, pageable).map(ActivitylogResponse::new);
        }
        else if(targetId != null) {
            return activitylogRepository.findByTaskId(targetId, pageable).map(ActivitylogResponse::new);
        }
        return activitylogRepository.findAll(pageable).map(ActivitylogResponse::new);
    }
}
