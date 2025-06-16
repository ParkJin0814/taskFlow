package com.example.taskflow.domain.user.service;

import com.example.taskflow.config.JwtUtil;
import com.example.taskflow.domain.common.dto.ApiResponse;
import com.example.taskflow.domain.user.dto.request.LoginRequest;
import com.example.taskflow.domain.user.dto.request.RegisterRequest;
import com.example.taskflow.domain.user.dto.response.LoginResponse;
import com.example.taskflow.domain.user.dto.response.RegisterResponse;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.exception.AuthException;
import com.example.taskflow.domain.user.exception.InvalidRequestException;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public ApiResponse register(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new InvalidRequestException("이미 가입 된 아이디입니다.");
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
        String bearerToken = jwtUtil.generateToken(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());

        return ApiResponse.ok("회원가입이 완료되었습니다.", new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole(),
                savedUser.getCreatedAt(),
                bearerToken
        ));
    }

    @Transactional
    public ApiResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidRequestException("가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole());


        return ApiResponse.ok("로그인이 완료되었습니다.", new LoginResponse(bearerToken));
    }
}
