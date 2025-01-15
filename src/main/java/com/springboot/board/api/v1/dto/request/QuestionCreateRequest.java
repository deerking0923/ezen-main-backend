package com.springboot.board.api.v1.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class QuestionCreateRequest {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 200)
    private String subject;

    // NotBlank는 공백을 허용하지 않기 때문에, 내용이 비어있거나 공백만 있는 경우를 방지
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;

    @NotBlank(message = "작성자 필수 입력값입니다.")
    @Size(max = 200)
    private String author; // 작성자

    // QuestionCreateRequest.java
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 4, max = 10, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;

}