package com.springboot.board.api.v1.dto.request;

import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
public class AnswerCreateRequest {
    @NotBlank
    private String content;
    @NotNull
    private Long questionId;
    @NotNull
    private Long userId;
}
