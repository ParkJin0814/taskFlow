package com.example.taskflow.domain.user.service;

import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.exception.BaseException;
import com.example.taskflow.domain.common.exception.ErrorCode;
import com.example.taskflow.domain.user.dto.response.MyProfileResponse;
import com.example.taskflow.domain.user.entity.User;
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
                .orElseThrow(()-> new BaseException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            throw new BaseException(ErrorCode.USER_DEACTIVATED);
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

}
