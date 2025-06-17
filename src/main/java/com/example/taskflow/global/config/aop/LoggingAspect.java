package com.example.taskflow.global.config.aop;

import com.example.taskflow.domain.activitylog.entity.ActivityLog;
import com.example.taskflow.domain.activitylog.enums.ActivityType;
import com.example.taskflow.domain.activitylog.repository.ActivitylogRepository;
import com.example.taskflow.domain.auth.dto.request.LoginRequest;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import com.example.taskflow.global.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    @Pointcut("@annotation(com.example.taskflow.global.config.aop.Logging)")
    public void loggingAnnotation() {}

    @Around("loggingAnnotation()")
    @Transactional
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new NullPointerException();
        }
        HttpServletRequest request = attributes.getRequest();
        LocalDateTime requestTime = LocalDateTime.now();
        String ipAddress = request.getRemoteAddr();
        String requestMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String check = requestMethod + requestUrl;
        ActivityType activityType = ActivityType.type(check);
        int taskId = activityType.getTaskId();
        User user = null;
        Long userId = null;

        if(activityType.equals(ActivityType.USER_LOGGED_IN)) {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof LoginRequest loginRequest) {
                    String username = loginRequest.getUsername();
                    user = userRepository.findByUsername(username).orElseThrow();
                    userId = user.getId();
                }
            }
        }
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            userId = principal.getUserId();
            user = userRepository.findById(userId).orElseThrow();
        }

        ActivityLog activityLog = new ActivityLog(requestTime, user, ipAddress, requestMethod, requestUrl, activityType, taskId);
        activitylogRepository.save(activityLog);

        log.info("requestTime = {} , userId = {}, ipAddress = {}, requestMethod = {}, requestUrl = {}, activityType = {}, targetId = {}"
        , requestTime, userId, ipAddress, requestMethod, requestUrl, activityType, taskId);
        return proceed;
    }
}
