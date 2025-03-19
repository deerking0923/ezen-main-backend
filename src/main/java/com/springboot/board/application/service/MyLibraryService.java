package com.springboot.board.application.service;

import com.springboot.board.api.v1.dto.UserBookDto;
import java.util.List;

public interface MyLibraryService {
    UserBookDto createUserBook(UserBookDto userBookDto);
    List<UserBookDto> getUserBooks(Long userId);
    UserBookDto getUserBook(Long userId, Long bookId);
    boolean deleteUserBook(Long userId, Long bookId);
    UserBookDto updateUserBook(UserBookDto userBookDto);
}
