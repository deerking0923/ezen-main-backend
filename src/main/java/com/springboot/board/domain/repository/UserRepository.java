package com.springboot.board.domain.repository;

import com.springboot.board.domain.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // 필요한 경우 커스텀 메서드 추가 가능
}
