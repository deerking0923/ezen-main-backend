package com.springboot.board.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Context;
import com.springboot.board.domain.entity.Answer;
import com.springboot.board.api.v1.dto.request.AnswerCreateRequest;
import com.springboot.board.api.v1.dto.response.AnswerResponse;
import com.springboot.board.application.service.QuestionService;

// AnswerMapper 인터페이스는 AnswerCreateRequest를 Answer 엔티티로 변환하고, Answer 엔티티를 AnswerResponse로 변환하는 매핑을 정의
@Mapper(componentModel = "spring")
public interface AnswerMapper {

    // AnswerCreateRequest의 questionId를 사용하여 Question 엔티티를 매핑
    @Mapping(target = "question", expression = "java(questionService.getQuestionEntity(request.getQuestionId()))")
    Answer toEntity(AnswerCreateRequest request, @Context QuestionService questionService);

    // Answer 엔티티를 AnswerResponse로 변환
    AnswerResponse toResponse(Answer answer);
}