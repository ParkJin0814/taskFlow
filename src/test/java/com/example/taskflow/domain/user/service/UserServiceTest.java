package com.example.taskflow.domain.user.service;

import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.exception.BaseException;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // 공통 given
    com.example.taskflow.domain.user.entity.User user = mock(com.example.taskflow.domain.user.entity.User.class);
    UserDetails userDetails = User.builder()
            .username("테스트")
            .password("")
            .authorities("ROLE_USER")
            .build();


    @Test
    @DisplayName("myProfile - 회원 조회 예외")
    void 유저_정보_찾고_없으면_예외처리() {

        // given

        // when
        BaseException e = Assertions.assertThrows(BaseException.class, () -> userService.myProfile(userDetails));

        // then
        assertEquals("사용자를 찾을 수 없습니다", e.getMessage());

    }

    @Test
    @DisplayName("myProfile - 삭제 회원 예외")
    void 유저_정보_있는데_탈퇴회원이면_예외처리() {

        // given
        given(userRepository.findByUsername("테스트")).willReturn(Optional.of(user));
        doReturn(true).when(user).isDeleted();

        // when
        BaseException e = Assertions.assertThrows(BaseException.class, () -> userService.myProfile(userDetails));

        // then
        assertEquals("탈퇴 된 회원입니다.", e.getMessage());

    }

    @Test
    @DisplayName("myProfile - 정상 처리")
    void 유저_정보_있고_탈퇴회원_아니면_회원_정보_반환() {

        // given
        given(userRepository.findByUsername("테스트")).willReturn(Optional.ofNullable(user));

        // when
        ApiResponse response = userService.myProfile(userDetails);

        // then
        assertEquals("사용자 정보를 조회했습니다.", response.getMessage());

    }


}