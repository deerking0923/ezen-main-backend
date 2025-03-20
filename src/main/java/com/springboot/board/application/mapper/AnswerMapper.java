package com.springboot.board.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.springboot.board.api.v1.dto.request.AnswerCreateRequest;
import com.springboot.board.api.v1.dto.response.AnswerResponse;
import com.springboot.board.domain.entity.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    // Request -> Entity (question, user는 서비스 계층에서 주입)
    Answer toEntity(AnswerCreateRequest request);

    // Entity -> Response
    @Mapping(target = "authorUsername", expression = "java(answer.getUser() != null ? answer.getUser().getUsername() : null)")
    AnswerResponse toResponse(Answer answer);
}
