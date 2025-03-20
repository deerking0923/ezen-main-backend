package com.springboot.board.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.springboot.board.domain.repository.AnswerRepository;
import com.springboot.board.domain.repository.QuestionRepository;
import com.springboot.board.domain.repository.UserRepository;
import com.springboot.board.api.v1.dto.request.AnswerCreateRequest;
import com.springboot.board.api.v1.dto.request.AnswerUpdateRequest;
import com.springboot.board.api.v1.dto.response.AnswerResponse;
import com.springboot.board.application.mapper.AnswerMapper;
import com.springboot.board.common.exception.DataNotFoundException;
import com.springboot.board.domain.entity.Answer;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepo;
    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;
    private final AnswerMapper mapper;

    @Transactional
    public AnswerResponse createAnswer(AnswerCreateRequest req) {
        var question = questionRepo.findById(req.getQuestionId())
                .orElseThrow(() -> new DataNotFoundException("Question not found"));
        var user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        var answer = mapper.toEntity(req);
        answer.setQuestion(question);
        answer.setUser(user);
        System.out.println("User's username: " + answer.getUser().getUsername());
        return mapper.toResponse(answerRepo.save(answer));
    }

    @Transactional
    public AnswerResponse updateAnswer(Long id, AnswerUpdateRequest req) {
        Answer answer = answerRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Answer not found"));
        answer.setContent(req.getContent());
        return mapper.toResponse(answerRepo.save(answer));
    }

    @Transactional
    public void deleteAnswer(Long id) {
        if (!answerRepo.existsById(id)) {
            throw new DataNotFoundException("Answer not found");
        }
        answerRepo.deleteById(id);
    }
}
