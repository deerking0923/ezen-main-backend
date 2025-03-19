package com.springboot.board.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.springboot.board.domain.repository.QuestionRepository;
import com.springboot.board.domain.repository.UserRepository;
import com.springboot.board.api.v1.dto.request.QuestionCreateRequest;
import com.springboot.board.api.v1.dto.request.QuestionUpdateRequest;
import com.springboot.board.api.v1.dto.response.QuestionResponse;
import com.springboot.board.application.mapper.QuestionMapper;
import com.springboot.board.common.exception.DataNotFoundException;
import com.springboot.board.domain.entity.Question;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;
    private final QuestionMapper mapper;

    @Transactional
    public QuestionResponse createQuestion(QuestionCreateRequest req) {
        var user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        // MapStruct로 subject, content 등을 매핑
        var q = mapper.toEntity(req);

        // user 주입
        q.setUser(user);

        return mapper.toResponse(questionRepo.save(q));
    }

    @Transactional(readOnly = true)
    public Page<QuestionResponse> getQuestions(int page) {
        // 정렬 조건: 생성일 DESC, 페이지 크기 10
        return questionRepo.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate")))
                .map(mapper::toResponse);
    }

    @Transactional
    public QuestionResponse getQuestion(Long id) {
        Question q = questionRepo.findByIdWithAnswers(id)
                .orElseThrow(() -> new DataNotFoundException("Question not found"));
        // 조회수 증가
        q.incrementViewCount();
        questionRepo.save(q);
        return mapper.toResponse(q);
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionUpdateRequest req) {
        Question q = questionRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Question not found"));

        q.setSubject(req.getSubject());
        q.setContent(req.getContent());

        return mapper.toResponse(questionRepo.save(q));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepo.existsById(id)) {
            throw new DataNotFoundException("Question not found");
        }
        questionRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public QuestionResponse getRandomQuestion() {
        var list = questionRepo.findAll();
        if (list.isEmpty()) throw new DataNotFoundException("No questions");
        return mapper.toResponse(list.get((int)(Math.random() * list.size())));
    }
}
