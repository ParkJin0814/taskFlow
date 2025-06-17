package com.example.taskflow.domain.auth.service;

import com.example.taskflow.domain.auth.dto.request.LoginRequest;
import com.example.taskflow.domain.auth.dto.request.RegisterRequest;
import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.exception.BaseException;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.UserRole;
import com.example.taskflow.domain.user.repository.UserRepository;
import com.example.taskflow.global.config.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;



    // register 공통 given
    RegisterRequest registerRequest = new RegisterRequest(
            "테스트", "a@a.com", "password1!", "password1!", "테스트"
    );

    @Test
    @DisplayName("register - 회원 조회 예외")
    void 가입된_username_있으면_예외처리() {

        // given
        given(userRepository.existsByUsername(registerRequest.getUsername())).willReturn(true);

        // when
        BaseException e = Assertions.assertThrows(BaseException.class, () -> authService.register(registerRequest));

        // then
        assertEquals("이미 존재하는 사용자명입니다", e.getMessage());

    }

    @Test
    @DisplayName("register - 삭제 회원 예외")
    void 가입된_email_있으면_예외처리() {

        // given
        given(userRepository.existsByUsername(registerRequest.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(registerRequest.getEmail())).willReturn(true);

        // when
        BaseException e = Assertions.assertThrows(BaseException.class, () -> authService.register(registerRequest));

        // then
        assertEquals("이미 존재하는 이메일입니다", e.getMessage());

    }

    @Test
    @DisplayName("register - 정상 처리")
    void 가입된_정보_없으면_회원가입() {

        // given
        given(userRepository.existsByUsername(registerRequest.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(registerRequest.getEmail())).willReturn(false);

        when(userRepository.save(any())).thenReturn(User.builder().build());

        // when
        ApiResponse response = authService.register(registerRequest);

        // then
        assertEquals("회원가입이 완료되었습니다.", response.getMessage());

    }



    // login 공통 given
    User user = mock(User.class);
    LoginRequest loginRequest = new LoginRequest("테스트", "password1!");


    @Test
    @DisplayName("login - 회원 조회 예외")
    void 유저_정보_찾고_없다면_예외처리() {

        // given

        // when
        BaseException e = Assertions.assertThrows(BaseException.class, () -> authService.login(loginRequest));

        // then
        assertEquals("사용자를 찾을 수 없습니다", e.getMessage());

    }

    @Test
    @DisplayName("login - 삭제 회원 예외")
    void 유저_정보_있는데_탈퇴_회원이면_예외처리() {

        // given
        given(userRepository.findByUsername(loginRequest.getUsername())).willReturn(Optional.ofNullable(user));
        doReturn(true).when(user).isDeleted();

        // when
        BaseException e = Assertions.assertThrows(BaseException.class, () -> authService.login(loginRequest));

        // then
        assertEquals("탈퇴 된 회원입니다.", e.getMessage());

    }

    @Test
    @DisplayName("login - 패스워드 불일치")
    void 유저_정보_있고_탈퇴회원_아니고_패스워드_불일치_예외처리() {

        // given
        given(userRepository.findByUsername(loginRequest.getUsername())).willReturn(Optional.ofNullable(user));
        doReturn(false).when(user).isDeleted();
        given(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).willReturn(false);

        // when
        BaseException e = Assertions.assertThrows(BaseException.class, () -> authService.login(loginRequest));

        // then
        assertEquals("비밀번호가 일치하지 않습니다", e.getMessage());

    }

    @Test
    @DisplayName("login - 정상 처리")
    void 유저_정보_있고_탈퇴회원_아니면_로그인() {

        // given
        given(userRepository.findByUsername(loginRequest.getUsername())).willReturn(Optional.ofNullable(user));
        doReturn(false).when(user).isDeleted();
        given(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).willReturn(true);

        doReturn(UserRole.USER).when(user).getRole();
        given(jwtUtil.generateToken(any(), any(), any(), any())).willReturn("token");

        // when
        ApiResponse response = authService.login(loginRequest);

        // then
        assertEquals("로그인이 완료되었습니다.", response.getMessage());

    }
}