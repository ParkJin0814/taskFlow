
package com.example.taskflow.domain.task.Controller;

import com.example.taskflow.domain.task.controller.TaskController;
import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.domain.task.dto.request.TaskStatusUpdateRequestDto;
import com.example.taskflow.domain.task.dto.request.TaskUpdateRequestDto;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    void 태스크_생성_성공() throws Exception {
        TaskCreateRequestDto request = new TaskCreateRequestDto("제목", "설명", TaskPriority.LOW, 1L,LocalDate.now().plusDays(1), LocalDate.now(), TaskStatus.TODO);
        TaskResponseDto response = new TaskResponseDto(1L, "제목", "설명", TaskPriority.LOW, "담당자", "생성자", LocalDate.now(), LocalDate.now().plusDays(1), TaskStatus.IN_PROGRESS,LocalDateTime.now());

        Mockito.when(taskService.createTask(any(TaskCreateRequestDto.class),any(Long.class))).thenReturn(response);

        ResultActions result = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("제목"));
    }

    @Test
    void 태스크_상태_업데이트() throws Exception {
        TaskStatusUpdateRequestDto request = new TaskStatusUpdateRequestDto(TaskStatus.IN_PROGRESS);
        TaskResponseDto response = new TaskResponseDto(1L, "제목", "설명", TaskPriority.LOW, "담당자", "생성자", LocalDate.now(), LocalDate.now().plusDays(1), TaskStatus.IN_PROGRESS,LocalDateTime.now());

        Mockito.when(taskService.updateTaskStatus(any(TaskStatusUpdateRequestDto.class), any(Long.class))).thenReturn(response);

        ResultActions result = mockMvc.perform(patch("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }

    @Test
    void 태스크_수정_테스트() throws Exception {
        TaskUpdateRequestDto request = new TaskUpdateRequestDto(TaskPriority.LOW, 1L, LocalDate.now().plusDays(2));
        TaskResponseDto response = new TaskResponseDto(1L, "제목", "설명", TaskPriority.LOW, "담당자", "생성자", LocalDate.now(), LocalDate.now().plusDays(1), TaskStatus.IN_PROGRESS,LocalDateTime.now());

        Mockito.when(taskService.updateTask(any(Long.class), any(TaskUpdateRequestDto.class))).thenReturn(response);

        ResultActions result = mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.deadLine").exists());
    }

    @Test
    void 테스크_삭제_테스트() throws Exception {
        ResultActions result = mockMvc.perform(delete("/api/tasks/1"));

        result.andExpect(status().isOk());
        Mockito.verify(taskService).deleteTask(1L);
    }
}
