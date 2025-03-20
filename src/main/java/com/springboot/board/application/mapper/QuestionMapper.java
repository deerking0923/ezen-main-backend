package com.springboot.board.application.mapper;

import java.util.List;
import com.springboot.board.api.v1.dto.request.QuestionCreateRequest;
import com.springboot.board.api.v1.dto.response.QuestionResponse;
import com.springboot.board.domain.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AnswerMapper.class)
public interface QuestionMapper {

    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "answers", ignore = true)
    Question toEntity(QuestionCreateRequest request);

    @Mapping(target = "authorUsername", source = "user.username")
    @Mapping(target = "answers", source = "answers")
    QuestionResponse toResponse(Question question);

    List<com.springboot.board.api.v1.dto.response.AnswerResponse> toAnswerResponses(List<com.springboot.board.domain.entity.Answer> answers);
}
