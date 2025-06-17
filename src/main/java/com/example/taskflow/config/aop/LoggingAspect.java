package com.example.taskflow.config.aop;

import com.example.taskflow.domain.activitylog.entity.ActivityLog;
import com.example.taskflow.domain.activitylog.enums.ActivityType;
import com.example.taskflow.domain.activitylog.repository.ActivitylogRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {
    private final ActivitylogRepository activitylogRepository;
    private final UserRepository userRepository;

    @Pointcut("@annotation(Logging)")
    public void loggingAnnotation() {}

    @AfterReturning("loggingAnnotation()")
    public void log() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new NullPointerException();
        }
        HttpServletRequest request = attributes.getRequest();
        LocalDateTime requestTime = LocalDateTime.now();
        Long userId = 1L;
        User user = userRepository.findById(userId).orElseThrow();
        String ipAddress = request.getRemoteAddr();
        String requestMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String check = requestMethod + requestUrl;
        ActivityType activityType = ActivityType.type(check);
        int taskId = activityType.getTaskId();

        ActivityLog activityLog = new ActivityLog(requestTime, user, ipAddress, requestMethod, requestUrl, activityType, taskId);
        activitylogRepository.save(activityLog);

        log.info("requestTime = {} , userId = {}, ipAddress = {}, requestMethod = {}, requestUrl = {}, activityType = {}, targetId = {}"
        , requestTime, userId, ipAddress, requestMethod, requestUrl, activityType, taskId);

    }
}
