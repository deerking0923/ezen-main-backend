package com.springboot.board.domain.repository;

import com.springboot.board.domain.entity.UserBookEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserBookRepository extends CrudRepository<UserBookEntity, Long> {
    List<UserBookEntity> findByUserId(Long userId);
}
