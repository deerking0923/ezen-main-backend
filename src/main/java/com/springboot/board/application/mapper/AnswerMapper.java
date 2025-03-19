package com.springboot.board.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.springboot.board.api.v1.dto.request.AnswerCreateRequest;
import com.springboot.board.api.v1.dto.response.AnswerResponse;
import com.springboot.board.domain.entity.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    // Request -> Entity (문자열 content 정도만 매핑; question, user는 서비스 계층에서 주입)
    Answer toEntity(AnswerCreateRequest request);

    // Entity -> Response
    @Mapping(target = "authorUsername", source = "user.username")
    AnswerResponse toResponse(Answer answer);
}
