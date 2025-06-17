package com.example.taskflow.domain.activitylog.repository;

import com.example.taskflow.domain.activitylog.entity.ActivityLog;
import com.example.taskflow.domain.activitylog.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitylogRepository extends JpaRepository<ActivityLog, Long> {
    Page<ActivityLog> findByUserId_Id(Long userId, Pageable pageable);

    Page<ActivityLog> findByActivityTypeAndTaskId(ActivityType activityType, Integer targetId, Pageable pageable);

    Page<ActivityLog> findByActivityType(ActivityType activityType, Pageable pageable);

    Page<ActivityLog> findByTaskId(Integer targetId, Pageable pageable);
}
