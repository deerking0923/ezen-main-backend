package com.springboot.board.application.service;

import com.springboot.board.api.v1.dto.UserBookDto;
import com.springboot.board.domain.entity.User;
import com.springboot.board.domain.repository.UserBookRepository;
import com.springboot.board.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MyLibraryServiceImpl implements MyLibraryService {

    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;

    public MyLibraryServiceImpl(UserBookRepository userBookRepository, UserRepository userRepository) {
        this.userBookRepository = userBookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserBookDto createUserBook(UserBookDto userBookDto) {
        // 기존 로직 구현
        return null;
    }

    @Override
    public List<UserBookDto> getUserBooks(Long userId) {
        // 기존 로직 구현
        return null;
    }

    @Override
    public UserBookDto getUserBook(Long userId, Long bookId) {
        // 기존 로직 구현
        return null;
    }

    @Override
    public boolean deleteUserBook(Long userId, Long bookId) {
        // 기존 로직 구현
        return false;
    }

    @Override
    public UserBookDto updateUserBook(UserBookDto userBookDto) {
        // 기존 로직 구현
        return null;
    }

    @Override
    public Long getUserIdByUsername(String username) {
        // UserRepository에서 username으로 User 엔티티를 조회합니다.
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null ? user.getId() : null;
    }
}
