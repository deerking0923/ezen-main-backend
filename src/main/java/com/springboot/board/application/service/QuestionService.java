package com.springboot.board.application.service;

//DTO를 사용하여 입력과 출력을 명확히 하여 API의 계약을 강화. 이는 데이터의 유효성을 검사하고, API의 일관성을 높이는 데 기여
//트랜잭션 관리:
//원본에서는 `create` 메서드에 트랜잭션 관리가 없었으나, 수정된 클래스에서는 `@Transactional` 어노테이션이 추가
//데이터베이스의 일관성을 보장하기 위해 트랜잭션을 명시적으로 관리. @Transactional(readOnly = true)는 읽기 전용 트랜잭션을 설정하여 성능을 최적화

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.springboot.board.domain.repository.QuestionRepository;
import com.springboot.board.api.v1.dto.request.QuestionCreateRequest;
import com.springboot.board.api.v1.dto.request.QuestionUpdateRequest;
import com.springboot.board.api.v1.dto.response.QuestionResponse;
import com.springboot.board.application.mapper.QuestionMapper;
import com.springboot.board.domain.entity.Question;
import com.springboot.board.common.exception.DataNotFoundException;

import org.springframework.data.domain.PageRequest;

@Service
// Transactional(readOnly = true)은 데이터베이스의 읽기 전용 트랜잭션을 설정하여 성능을 최적화하고, 데이터의 일관성을
// 보장하기 위해 사용
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    // method 이름 변경: create → createQuestion
    // createQuestion 메서드는 QuestionCreateRequest를 받아 Question 엔티티로 변환한 후,
    // 데이터베이스에 저장하고, 저장된 질문을 QuestionResponse로 변환하여 반환.
    @Transactional
    public QuestionResponse createQuestion(QuestionCreateRequest request) {

        // 요청을 기반으로 Question 엔티티 생성
        Question question = questionMapper.toEntity(request);

        // Question 엔티티를 데이터베이스에 저장
        Question savedQuestion = questionRepository.save(question);

        // 저장된 Question 엔티티를 QuestionResponse DTO로 변환하여 반환
        return questionMapper.toResponse(savedQuestion);
    }

    // method 이름 변경:getList → getQuestions
    // getQuestions 메서드는 페이지 번호를 받아 해당 페이지의 질문 목록을 반환.
    // Pageable 객체를 생성하여 페이지 크기와 정렬 기준을 설정.
    public Page<QuestionResponse> getQuestions(int page) {

        // 페이지 요청을 생성하고, 정렬 기준으로 생성일자를 내림차순으로 설정
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createDate"));

        // 질문 목록을 데이터베이스에서 조회
        Page<Question> questions = questionRepository.findAll(pageable);

        // Question 엔티티를 QuestionResponse DTO로 변환하여 반환
        // question -> questionMapper.toResponse(question) 표현식과 동일
        // question은 컴파일러가 자동으로 추론하여 제공
        return questions.map(questionMapper::toResponse);
    }

    // method 이름 변경:getQuestion → getQuestion
    // getQuestion 메서드는 주어진 ID로 질문을 조회하고,
    // 해당 질문이 존재하지 않을 경우 DataNotFoundException을 발생.
    @Transactional
    public QuestionResponse getQuestion(Integer id) {
        Question question = questionRepository.findByIdWithAnswers(id)
                .orElseThrow(() -> new DataNotFoundException("질문을 찾을 수 없습니다."));
        question.incrementViewCount(); // 조회수 증가
        questionRepository.save(question); // 데이터베이스에 반영
        return questionMapper.toResponse(question);
    }

    @Transactional
    private void increaseViewCount(Question question) {
        question.incrementViewCount(); // 엔티티 메서드 호출로 ViewCount 증가
        questionRepository.save(question); // 변경사항 저장
    }

    // Entity 조회를 위한 메서드 추가
    public Question getQuestionEntity(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("질문을 찾을 수 없습니다."));
    }

    @Transactional
    public QuestionResponse updateQuestion(Integer id, QuestionUpdateRequest request) {

        // ID를 기반으로 기존 질문을 조회
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        // 기존 질문 정보를 업데이트
        question.setSubject(request.getSubject());
        question.setContent(request.getContent());
        question.setAuthor(request.getAuthor()); // author도 수정 가능

        // 변경된 질문을 데이터베이스에 저장
        Question updatedQuestion = questionRepository.save(question);

        // 업데이트된 질문을 QuestionResponse DTO로 변환하여 반환
        return questionMapper.toResponse(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Integer id) {
        try {
            if (!questionRepository.existsById(id)) {
                throw new DataNotFoundException("질문을 찾을 수 없습니다. id: " + id);
            }
            questionRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("질문 삭제 중 오류가 발생했습니다. 오류 메시지: " + e.getMessage(), e);
        }
    }

    // 랜덤 문장 조회
    // 랜덤 질문을 반환하는 메서드 추가
    @Transactional(readOnly = true)
    public QuestionResponse getRandomQuestion() {
        List<Question> questions = questionRepository.findAll(); // 모든 질문을 리스트로 가져옴
        if (questions.isEmpty()) {
            throw new DataNotFoundException("질문이 없습니다.");
        }
        Random rand = new Random();
        Question randomQuestion = questions.get(rand.nextInt(questions.size())); // 랜덤으로 질문 선택
        return questionMapper.toResponse(randomQuestion); // 선택된 질문을 DTO로 반환
    }

    // 비밀먼호가 같은지 확인하는 메소드
    @Transactional(readOnly = true)
    public boolean checkPassword(Integer id, String inputPassword) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("질문을 찾을 수 없습니다."));
        return question.getPassword().equals(inputPassword);
    }

}
