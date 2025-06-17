package com.example.taskflow.domain.task.Repository;

import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.UserRole;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 태스크_저장_로직_테스트() {
        //given
        User user = new User(null, "사용자1", "kmg02845@gmail.com", "12341234", "사용자1", UserRole.of("USER"));
        User user2 = new User(null, "사용자2", "2222@gmail.com", "12341234", "사용자2", UserRole.of("USER"));

        userRepository.save(user);
        userRepository.save(user2);

        Task task = new Task(null, "제목", "내용", TaskPriority.LOW, user, user2,
                LocalDate.now(), LocalDate.now().plusDays(7), TaskStatus.TODO);

        //when
        Task saved = taskRepository.save(task);

        //then
        Assertions.assertThat(saved.getAssignedUser().getUserName()).isEqualTo("사용자1");
    }

    @Test
    void 상태별_조회_테스트() {
        // given
        User user = new User(null, "유저", "u@test.com", "pw", "user", UserRole.USER);
        userRepository.save(user);

        Task task1 = new Task(null, "t1", "내용1", TaskPriority.LOW, user, user,
                LocalDate.now(), LocalDate.now().plusDays(7), TaskStatus.TODO);
        Task task2 = new Task(null, "t2", "내용2", TaskPriority.MEDIUM, user, user,
                LocalDate.now(), LocalDate.now().plusDays(7), TaskStatus.TODO);
        Task task3 = new Task(null, "t3", "내용3", TaskPriority.HIGH, user, user,
                LocalDate.now(), LocalDate.now().plusDays(7), TaskStatus.DONE);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // when
        List<Task> todoTasks = taskRepository.findAllByStatusAndIsDeletedFalse(TaskStatus.TODO, Pageable.ofSize(10)).getContent();

        // then
        Assertions.assertThat(todoTasks).hasSize(2);
    }

    @Test
    void 삭제되지_않은_태스크_전체_조회_테스트() {
        // given
        User user = new User(null, "user", "email", "pw", "nickname", UserRole.USER);
        userRepository.save(user);

        taskRepository.save(new Task(null, "t1", "내용1", TaskPriority.LOW, user, user,
                LocalDate.now(),LocalDate.now().plusDays(7), TaskStatus.TODO));
        taskRepository.save(new Task(null, "t2", "내용2", TaskPriority.LOW, user, user,
                LocalDate.now(), LocalDate.now().plusDays(7), TaskStatus.DONE));

        // when
        List<Task> tasks = taskRepository.findAllByIsDeletedIsFalse(Pageable.ofSize(10)).getContent();

        // then
        Assertions.assertThat(tasks).hasSize(1);
    }
}
