package com.springboot.board.domain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.board.domain.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}