package com.springboot.board.domain.repository;

import com.springboot.board.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
        SELECT q
        FROM Question q
        LEFT JOIN FETCH q.answers a
        LEFT JOIN FETCH a.user
        WHERE q.id = :id
        """)
    Optional<Question> findByIdWithAnswers(@Param("id") Long id);
    
}
