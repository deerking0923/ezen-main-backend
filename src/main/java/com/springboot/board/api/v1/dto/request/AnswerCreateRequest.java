package com.springboot.board.api.v1.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AnswerCreateRequest {
    @NotBlank(message = "내용은 반드시 입력하셔야 합니다.")
    private String content;
    private Integer questionId;
}