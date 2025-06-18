package com.example.taskflow.global.exception;

import com.example.taskflow.domain.auth.controller.AuthController;
import com.example.taskflow.domain.auth.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(AuthController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void 유효성_검사_예외_테스트() throws Exception {
        // Given
        LoginRequest dto = new LoginRequest("김땡땡", null); // name이 null이라서 실패


        // When

        // Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON) // 요청이 json 형식인걸 명시
                        .content(objectMapper.writeValueAsString(dto))) // 객체를 json 문자열로 변환해서 http에 담아준다
                .andExpect(status().isBadRequest()) // 상태값이 badRequest인지 확인
                .andExpect(jsonPath("$.success").value(false)) // success 값이 false인지 확인
                .andExpect(jsonPath("$.message", containsString("비밀번호를 입력해주세요"))); // 문구가 포함되어있는지 확인
    }
}
