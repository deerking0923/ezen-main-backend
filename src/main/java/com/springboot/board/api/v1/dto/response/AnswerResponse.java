package com.springboot.board.api.v1.dto.response;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.springboot.board.domain.entity.Answer;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private Integer id;
    private String content;
    private LocalDateTime createDate;

    public AnswerResponse(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.createDate = answer.getCreateDate(); // getCreateDate()로 수정
    }
}