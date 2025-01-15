package com.springboot.board.application.mapper;

import java.util.List;
import com.springboot.board.api.v1.dto.request.QuestionCreateRequest;
import com.springboot.board.api.v1.dto.response.QuestionResponse;
import com.springboot.board.api.v1.dto.response.AnswerResponse;
import com.springboot.board.domain.entity.Question;
import com.springboot.board.domain.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    // QuestionCreateRequest에서 Question 엔티티로 변환
    @Mapping(target = "author", source = "author") // 작성자 매핑
    @Mapping(target = "viewCount", ignore = true)
    Question toEntity(QuestionCreateRequest request);

    // Question 엔티티에서 QuestionResponse로 변환
    @Mapping(target = "answers", source = "answers") // 답변 매핑
    @Mapping(target = "password", source = "password")
    QuestionResponse toResponse(Question question);

    // Answer 엔티티 리스트를 AnswerResponse 리스트로 변환
    List<AnswerResponse> toAnswerResponses(List<Answer> answers);

    // Answer 엔티티를 AnswerResponse로 변환
    AnswerResponse toAnswerResponse(Answer answer);
}
