package com.springboot.board.api.v1.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class QuestionResponse {
    private Long id;
    private String subject;
    private String content;
    private LocalDateTime createdDate;
    private Integer viewCount;
    private String authorUsername;      // user.username 매핑
    private List<AnswerResponse> answers;
}
