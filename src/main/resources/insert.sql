CREATE DATABASE IF NOT EXISTS taskFlow_db;

use taskFlow_db;

CREATE TABLE IF NOT EXISTS `users` (
                         `id`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY	COMMENT '유저 고유 식별자',
                         `username`	VARCHAR(100)	NOT NULL	COMMENT '로그인에 사용할 아이디',
                         `email`	VARCHAR(255)	NOT NULL UNIQUE COMMENT '이메일',
                         `password`	VARCHAR(255)	NOT NULL	COMMENT '비밀번호',
                         `name`	VARCHAR(100)	NOT NULL	    COMMENT '유저 본인 네임',
                         `role`  ENUM('USER', 'ADMIN') NULL COMMENT '우선순위',
                         `is_deleted`	BOOLEAN	NOT NULL DEFAULT FALSE	COMMENT '삭제 여부 플래그',
                         `deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일',
                         `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                         `updated_at`	TIMESTAMP	NOT NULL	COMMENT '수정일'
);

CREATE TABLE IF NOT EXISTS `tasks` (
                        `id`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT '태스크 고유 식별자',
                        `title`	VARCHAR(200)	NOT NULL	COMMENT '태스크의 이름',
                        `description`	TEXT	NOT NULL	COMMENT ' 태스크에 대한 상세 설명',
                        `priority` BIGINT NULL COMMENT '우선순위',
                        `status` ENUM('TODO', 'IN_PROGRESS', 'DONE') NOT NULL DEFAULT 'TODO' COMMENT '상태',
                        `assigned_user_id`	BIGINT	NOT NULL	COMMENT ' 태스크를 담당하는 사용자 id',
                        `created_user_id`	BIGINT	NOT NULL	COMMENT '태스크를 생성한 사용자 id',
                        `dead_line`	TIMESTAMP	NOT NULL	COMMENT '작업 마감일',
                        `start_line`	TIMESTAMP	NOT NULL	COMMENT '작업 시작일',
                        `is_deleted`	BOOLEAN	NOT NULL DEFAULT FALSE	COMMENT '삭제 여부 플래그',
                        `deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일',
                        `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                        `updated_at`	TIMESTAMP	NOT NULL	COMMENT '수정일',
                        FOREIGN KEY (assigned_user_id) REFERENCES users(id),
                        FOREIGN KEY (created_user_id) REFERENCES users(id)

);


CREATE TABLE IF NOT EXISTS `comment` (
                           `id`	BIGINT	AUTO_INCREMENT PRIMARY KEY	COMMENT '댓글 고유 식별자',
                           `task_id`	BIGINT	NOT NULL	COMMENT '태스크 고유 식별자',
                           `user_id`	BIGINT	NOT NULL	COMMENT '작성자 고유 식별자',
                           `content`	TEXT	NOT NULL	COMMENT '댓글',
                           `is_deleted`	BOOLEAN	NOT NULL DEFAULT FALSE	COMMENT '삭제 여부 플래그',
                           `created_at`	TIMESTAMP	NOT NULL	COMMENT '생성일',
                           `updated_at`	TIMESTAMP	NOT NULL	COMMENT '수정일',
                           `deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일',
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (task_id) REFERENCES tasks(id)
);

CREATE TABLE IF NOT EXISTS `activity_log` (
                          `id`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT '액티비티 로그 식별자',
                          `request_time`	TIMESTAMP	NOT NULL	COMMENT '요청시간',
                          `user_id`	BIGINT	NOT NULL	COMMENT '사용자 ID',
                          `ip_address`	VARCHAR(200)	NOT NULL	COMMENT ' IP 주소',
                          `request_method`	VARCHAR(200)	NOT NULL	COMMENT '요청 메서드',
                          `request_url`	VARCHAR(200)	NOT NULL	COMMENT '요청 URL',
                          `activity_type`	ENUM(
                              'TASK_CREATED',
                              'TASK_UPDATED',
                              'TASK_DELETED',
                              'TASK_STATUS_CHANGED',
                              'COMMENT_CREATED',
                              'COMMENT_UPDATED',
                              'COMMENT_DELETED',
                              'USER_LOGGED_IN',
                              'USER_LOGGED_OUT')	NOT NULL	COMMENT '활동 타입',
                          `target_id`	BIGINT	NOT NULL	COMMENT ' 대상 ID(1. 테스크 ID, 2. 댓글 ID, 사용자 ID)',
                          `target_type` ENUM('TASK', 'COMMENT', 'USER') NOT NULL COMMENT '대상 유형',
                          FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

