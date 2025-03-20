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
import org.springframework.security.core.Authentication; // 추가
import org.springframework.security.core.context.SecurityContextHolder; // 추가
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mylibrary-service")
@Slf4j
public class MyLibraryController {

    private final MyLibraryService myLibraryService;

    public MyLibraryController(MyLibraryService myLibraryService) {
        this.myLibraryService = myLibraryService;
    }

    @GetMapping("/booklist")
    public ResponseEntity<?> getUserBooks() {
        // SecurityContextHolder에서 현재 인증된 사용자 정보를 가져옴
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // principal이 문자열(username)인 경우
        String username = (String) auth.getPrincipal();
        // 서비스 계층에서 username을 이용해 userId를 조회하는 메서드를 호출합니다.
        Long userId = myLibraryService.getUserIdByUsername(username);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("GET booklist for userId: {}", userId);

        // 책 목록 조회
        List<UserBookDto> books = myLibraryService.getUserBooks(userId);
        // 책 목록이 null이거나 비어있으면 빈 배열을 반환
        if (books == null) {
            books = new ArrayList<>();
        }
        return ResponseEntity.ok(books);
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<ResponseUserBook> createUserBook(
            @PathVariable("userId") Long userId,
            @RequestBody RequestUserBook requestBook) {
        log.info("POST create book for userId: {}", userId);
        
        // 요청 객체가 null인지 검사
        if (requestBook == null) {
            throw new IllegalArgumentException("Request body is null");
        }
        // quotes 필드가 null이면 빈 리스트로 초기화
        if (requestBook.getQuotes() == null) {
            requestBook.setQuotes(new ArrayList<>());
        }
        
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT)
              .setSkipNullEnabled(true);
        
        // RequestUserBook을 UserBookDto로 매핑
        UserBookDto userBookDto = mapper.map(requestBook, UserBookDto.class);
        userBookDto.setUserId(userId);
        
        // 서비스 호출 - null 반환 여부 체크
        UserBookDto createdDto = myLibraryService.createUserBook(userBookDto);
        if (createdDto == null) {
            log.error("myLibraryService.createUserBook returned null for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // 생성된 DTO를 ResponseUserBook으로 매핑
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
        mapper.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT)
              .setSkipNullEnabled(true);
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
        
        if (requestBook == null) {
            throw new IllegalArgumentException("Request body is null");
        }
        if (requestBook.getQuotes() == null) {
            requestBook.setQuotes(new ArrayList<>());
        }
        
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT)
              .setSkipNullEnabled(true);
        
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
