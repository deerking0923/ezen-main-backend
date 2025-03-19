package com.springboot.board.domain.repository;

import com.springboot.board.domain.entity.BookQuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookQuoteRepository extends JpaRepository<BookQuoteEntity, Long> {
    List<BookQuoteEntity> findByUserBook_Id(Long userBookId);
}
