package com.springboot.board.api.v1.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class AnswerResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private String authorUsername;
}
