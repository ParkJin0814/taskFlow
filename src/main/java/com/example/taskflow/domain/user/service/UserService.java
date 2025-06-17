package com.example.taskflow.domain.user.service;

import com.example.taskflow.config.JwtUtil;
import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.exception.UsernameAlreadyExistsException;
import com.example.taskflow.domain.user.dto.request.LoginRequest;
import com.example.taskflow.domain.user.dto.request.RegisterRequest;
import com.example.taskflow.domain.user.dto.response.LoginResponse;
import com.example.taskflow.domain.user.dto.response.MyProfileResponse;
import com.example.taskflow.domain.user.dto.response.RegisterResponse;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.exception.AuthException;
import com.example.taskflow.domain.user.exception.InvalidRequestException;
import com.example.taskflow.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     *
     * @param registerRequest 회원가입 작성폼 (username, email, password, name)
     * @return 회원가입 된 유저 정보
     */
    @Transactional
    public ApiResponse register(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new InvalidRequestException("이미 가입 된 사용자명입니다.");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new InvalidRequestException("이미 가입 된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User newUser = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                encodedPassword,
                registerRequest.getName()
                );

        User savedUser = userRepository.save(newUser);

        return ApiResponse.ok("회원가입이 완료되었습니다.", new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        ));
    }

    /**
     * 로그인
     *
     * @param loginRequest 로그인 작성 폼 (username, password)
     * @return 로그인 된 유저 JWT토큰
     */
    @Transactional
    public ApiResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthException("가입되지 않은 유저입니다."));

        if (user.isDeleted()) {
            throw new InvalidRequestException("탈퇴된 계정은 불가합니다.");
        }

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole());

        return ApiResponse.ok("로그인이 완료되었습니다.", new LoginResponse(bearerToken));
    }

    /**
     * 내 프로필
     *
     * @param userDetails 로그인 된 유저 JWT토큰의 정보 (username 등)
     * @return 로그인 된 유저 정보
     */
    @Transactional
    public ApiResponse myProfile(UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new AuthException("가입되지 않은 유저입니다."));

        if (user.isDeleted()) {
            throw new InvalidRequestException("탈퇴된 계정은 불가합니다.");
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
                .orElseThrow(() -> new AuthException("가입되지 않은 유저입니다."));

        if (user.isDeleted()) {
            throw new InvalidRequestException("이미 탈퇴된 계정입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidRequestException("패스워드가 일치하지 않습니다.");
        }

        user.softDelete();
        return ApiResponse.ok("회원 탈퇴가 완료되었습니다.", null);

    }
}
