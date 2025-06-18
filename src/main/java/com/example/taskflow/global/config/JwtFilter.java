package com.example.taskflow.global.config;

import com.example.taskflow.domain.user.enums.UserRole;
import com.example.taskflow.global.dto.CustomUserDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // CORS preflight 요청은 무조건 통과
        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        String requestURI = httpRequest.getRequestURI();
        String username = null;
        String jwt = null;

        String authorizationHeader = httpRequest.getHeader("Authorization");

        // 고급 개발자

        // 처음 로그인 하는 거야? 그럼 JWT 토큰이 없을 것이니 토큰 먼저 발급 받아!
        if(requestURI.equals("/api/auth/register") || requestURI.equals("/api/auth/login")) {
            chain.doFilter(request,response);
            return;
        }

        // 로그인 하는게 아니네? 그럼 JWT 토큰 있어?

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("JWT 토큰이 필요 합니다.");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요 합니다.");
            return;
        }

        // JWT 토큰이 있구만 그럼 JWT 토큰 유효해?
        // 1. Secret Key 내가 만든 거랑 동일해?
        // 2. JWT 시간 만료 된거 아니야?

        jwt = authorizationHeader.substring(7);

        // Secret Key 는 내가 만든게 맞는지 검증 만료 기간 지났는지 검증
        // Key , 만료기간이 지났는지 지나지 않았는지
        if (!jwtUtil.validateToken(jwt)) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\": \"Unauthorized\"}");
        }

        // 그럼 너가 가져오 JWT 토큰은 유효한 토큰이군! 통과!


        // 이제부터는 JWT 토큰에 어떤 정보가 들어가 있는지 살펴보자!

        // JWT 사용자의 이름을 확인 해보자
        username = jwtUtil.extractUsername(jwt);
        Long userId = jwtUtil.extractUserId(jwt);

        // JWT 사용자 권한을 확인
        String auth = jwtUtil.extractRoles(jwt);
        UserRole userRole = UserRole.valueOf(auth);
        CustomUserDetails userDetails = new CustomUserDetails(
                userId,
                username,
                "", // 비밀번호는 필요 없음
                userRole
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        chain.doFilter(request, response);


    }
}
