package com.springboot.board.api.v1.dto.request;

import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Getter
public class QuestionUpdateRequest {
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
}
