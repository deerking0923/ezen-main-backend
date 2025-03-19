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

    // Request -> Entity
    // user 정보는 서비스 계층에서 주입(setUser)하고,
    // viewCount나 answers는 초기값 혹은 ignore 처리
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "answers", ignore = true)
    Question toEntity(QuestionCreateRequest request);

    // Entity -> Response
    // QuestionResponse에는 authorUsername 필드가 있으므로 user.username 매핑
    @Mapping(target = "authorUsername", source = "user.username")
    @Mapping(target = "answers", source = "answers")
    QuestionResponse toResponse(Question question);

    // Answer 리스트 -> AnswerResponse 리스트
    List<AnswerResponse> toAnswerResponses(List<Answer> answers);

    // Answer -> AnswerResponse
    // (굳이 필요 없다면 삭제 가능하나, 아래 answers 매핑시 사용할 수 있습니다)
    AnswerResponse toAnswerResponse(Answer answer);
}
