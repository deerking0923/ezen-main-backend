package com.springboot.board.api.v1.controller;

import com.springboot.board.api.v1.dto.UserBookDto;
import com.springboot.board.api.v1.dto.request.RequestUserBook;
import com.springboot.board.api.v1.dto.response.ResponseUserBook;
import com.springboot.board.application.service.MyLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mylibrary-service")
@Slf4j
public class MyLibraryController {

    private final MyLibraryService myLibraryService;

    public MyLibraryController(MyLibraryService myLibraryService) {
        this.myLibraryService = myLibraryService;
    }

    @GetMapping("/{userId}/booklist")
    public ResponseEntity<?> getUserBooks(@PathVariable("userId") Long userId) {
        log.info("GET booklist for userId: {}", userId);
        return ResponseEntity.ok(myLibraryService.getUserBooks(userId));
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<ResponseUserBook> createUserBook(
            @PathVariable("userId") Long userId,
            @RequestBody RequestUserBook requestBook) {
        log.info("POST create book for userId: {}", userId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserBookDto userBookDto = mapper.map(requestBook, UserBookDto.class);
        userBookDto.setUserId(userId);

        UserBookDto createdDto = myLibraryService.createUserBook(userBookDto);
        ResponseUserBook response = mapper.map(createdDto, ResponseUserBook.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}/book/{bookId}")
    public ResponseEntity<?> getBookDetail(
            @PathVariable("userId") Long userId,
            @PathVariable("bookId") Long bookId) {
        log.info("GET book detail - userId: {}, bookId: {}", userId, bookId);
        UserBookDto userBookDto = myLibraryService.getUserBook(userId, bookId);
        if (userBookDto == null) {
            return ResponseEntity.notFound().build();
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseUserBook response = mapper.map(userBookDto, ResponseUserBook.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/book/{bookId}")
    public ResponseEntity<?> deleteUserBook(
            @PathVariable("userId") Long userId,
            @PathVariable("bookId") Long bookId) {
        log.info("DELETE book record for userId: {}, bookId: {}", userId, bookId);
        boolean deleted = myLibraryService.deleteUserBook(userId, bookId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("삭제할 책 기록이 없습니다.");
        }
    }

    @PutMapping("/{userId}/book/{bookId}")
    public ResponseEntity<?> updateUserBook(
            @PathVariable("userId") Long userId,
            @PathVariable("bookId") Long bookId,
            @RequestBody RequestUserBook requestBook) {
        log.info("PUT update book for userId: {}, bookId: {}", userId, bookId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserBookDto userBookDto = mapper.map(requestBook, UserBookDto.class);
        userBookDto.setUserId(userId);
        userBookDto.setId(bookId);

        UserBookDto updatedDto = myLibraryService.updateUserBook(userBookDto);
        if (updatedDto == null) {
            return ResponseEntity.notFound().build();
        }
        ResponseUserBook response = mapper.map(updatedDto, ResponseUserBook.class);
        return ResponseEntity.ok(response);
    }
}
