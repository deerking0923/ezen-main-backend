package com.springboot.board.domain.repository;

import com.springboot.board.domain.entity.UserBookEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends CrudRepository<UserBookEntity, Long> {
    // 사용자 id로 조회 (User 엔티티의 id 필드를 이용)
    List<UserBookEntity> findByUser_Id(Long userId);
    
    // bookId와 user의 id로 단건 조회
    Optional<UserBookEntity> findByIdAndUser_Id(Long id, Long userId);
}
