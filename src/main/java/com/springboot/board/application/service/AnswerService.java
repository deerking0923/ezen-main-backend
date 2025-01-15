package com.springboot.board.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.springboot.board.domain.repository.AnswerRepository;
import com.springboot.board.api.v1.dto.request.AnswerCreateRequest;
import com.springboot.board.api.v1.dto.response.AnswerResponse;
import com.springboot.board.application.mapper.AnswerMapper;
import com.springboot.board.domain.entity.Answer;

// AnswerService 클래스는 답변 관련 비즈니스 로직을 처리하는 서비스 클래스
@Service
@Transactional(readOnly = true) // 읽기 전용 트랜잭션을 설정하여 성능을 최적화
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final QuestionService questionService;

    @Transactional // 트랜잭션을 사용하여 데이터베이스 작업을 수행
    public AnswerResponse createAnswer(AnswerCreateRequest request) {

        // AnswerCreateRequest를 Answer 엔티티로 변환
        Answer answer = answerMapper.toEntity(request, questionService);

        // 변환된 Answer 엔티티를 데이터베이스에 저장
        Answer savedAnswer = answerRepository.save(answer);

        // 저장된 Answer 엔티티를 AnswerResponse로 변환하여 반환
        return answerMapper.toResponse(savedAnswer);
    }
    /*
     * @Transactional
     * public AnswerResponse updateAnswer(Integer id, AnswerUpdateRequest request) {
     * // 답변 찾기
     * Answer answer = answerRepository.findById(id)
     * .orElseThrow(() -> new RuntimeException("답변을 찾을 수 없습니다.")); // 예외 처리 추가
     * 
     * // 답변 수정
     * answer.setContent(request.getContent());
     * answerRepository.save(answer);
     * 
     * return new AnswerResponse(answer); // 수정된 답변 반환
     * }
     */
}