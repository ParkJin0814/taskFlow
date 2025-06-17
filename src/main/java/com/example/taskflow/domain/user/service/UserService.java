package com.example.taskflow.domain.user.service;

import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.exception.InvalidCredentialsException;
import com.example.taskflow.domain.user.dto.response.MyProfileResponse;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.common.exception.custom.AuthException;
import com.example.taskflow.domain.common.exception.custom.InvalidRequestException;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 내 프로필
     *
     * @param userDetails 로그인 된 유저 JWT토큰의 정보 (username 등)
     * @return 로그인 된 유저 정보
     */
    @Transactional
    public ApiResponse myProfile(UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (user.isDeleted()) {
            throw new AuthException("탈퇴 된 회원입니다.");
        }

        return ApiResponse.ok("사용자 정보를 조회했습니다.", new MyProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        ));
    }

    /**
     * 회원 탈퇴
     *
     * @param userDetails 로그인 된 유저 JWT토큰의 정보 (username 등)
     * @param password 패스워드 확인
     * @return 회원 탈퇴 완료 메시지
     */
    @Transactional
    public ApiResponse deletion(UserDetails userDetails, String password) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (user.isDeleted()) {
            throw new AuthException("이미 탈퇴 된 회원입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidRequestException("패스워드가 일치하지 않습니다.");
        }

        user.softDelete();
        return ApiResponse.ok("회원 탈퇴가 완료되었습니다.", null);

    }
}
