package com.example.taskflow.domain.auth.service;

import com.example.taskflow.domain.auth.dto.request.LoginRequest;
import com.example.taskflow.domain.auth.dto.request.RegisterRequest;
import com.example.taskflow.domain.auth.dto.response.LoginResponse;
import com.example.taskflow.domain.auth.dto.response.RegisterResponse;
import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.common.exception.EmailAlreadyExistsException;
import com.example.taskflow.domain.common.exception.InvalidCredentialsException;
import com.example.taskflow.domain.common.exception.UsernameAlreadyExistsException;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import com.example.taskflow.global.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

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
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException();
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
                .orElseThrow(InvalidCredentialsException::new);

        if (user.isDeleted()) {
            throw new InvalidCredentialsException();
        }

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        //프론트에서 받을 때 "Bearer "를 제거하는 로직이 없음.
        //따라서 서버에서 줄 때 부터 붙일 필요가 없음.
        String bearerToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole()).substring(7);

        return ApiResponse.ok("로그인이 완료되었습니다.", new LoginResponse(bearerToken));
    }
}
