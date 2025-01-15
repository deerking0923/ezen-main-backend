package com.springboot.board.api.v1.dto.response;

import lombok.Getter;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class QuestionResponse {
    private Integer id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private List<AnswerResponse> answers;
    private String password;
    // 새 필드 추가
    private String author; // 작성자
    private Integer viewCount; // 조회수
}