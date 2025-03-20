package com.springboot.board.application.service;

import com.springboot.board.api.v1.dto.UserBookDto;
import com.springboot.board.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface MyLibraryService {
    UserBookDto createUserBook(UserBookDto userBookDto);
    List<UserBookDto> getUserBooks(Long userId);
    UserBookDto getUserBook(Long userId, Long bookId);
    boolean deleteUserBook(Long userId, Long bookId);
    UserBookDto updateUserBook(UserBookDto userBookDto);
    Long getUserIdByUsername(String username);
    
}
