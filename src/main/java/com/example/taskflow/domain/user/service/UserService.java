package com.example.taskflow.domain.user.service;

import com.example.taskflow.config.JwtUtil;
import com.example.taskflow.domain.user.dto.request.RegisterRequest;
import com.example.taskflow.domain.user.dto.response.RegisterResponse;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.exception.InvalidRequestException;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegisterResponse register(RegisterRequest registerRequest) {

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

        return new RegisterResponse(bearerToken);
    }

}
