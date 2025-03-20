package com.springboot.board.application.service;

import com.springboot.board.api.v1.dto.UserBookDto;
import com.springboot.board.api.v1.dto.BookQuoteDto;
import com.springboot.board.domain.entity.User;
import com.springboot.board.domain.entity.UserBookEntity;
import com.springboot.board.domain.entity.BookQuoteEntity;
import com.springboot.board.domain.repository.UserBookRepository;
import com.springboot.board.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
        // 1. 사용자 확인
        User user = userRepository.findById(userBookDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        
        // 2. 새로운 UserBookEntity 생성 및 필드 셋팅
        UserBookEntity userBook = new UserBookEntity();
        userBook.setUser(user);
        userBook.setIsbn(userBookDto.getIsbn());
        userBook.setTitle(userBookDto.getTitle());
        userBook.setAuthor(userBookDto.getAuthor());
        userBook.setPublisher(userBookDto.getPublisher());
        userBook.setPDate(userBookDto.getPDate());
        userBook.setDescription(userBookDto.getDescription());
        userBook.setThumbnail(userBookDto.getThumbnail());
        userBook.setPersonalReview(userBookDto.getPersonalReview());
        userBook.setStartDate(userBookDto.getStartDate());
        userBook.setEndDate(userBookDto.getEndDate());
        
        // 3. BookQuote 처리: DTO의 리스트를 엔티티 리스트로 변환
        if (userBookDto.getQuotes() != null) {
            List<BookQuoteEntity> quotes = userBookDto.getQuotes().stream().map(dto -> {
                BookQuoteEntity quote = new BookQuoteEntity();
                quote.setPageNumber(dto.getPageNumber());
                quote.setQuoteText(dto.getQuoteText());
                // 양방향 연관관계: BookQuoteEntity가 어떤 UserBookEntity에 속하는지 설정
                quote.setUserBook(userBook);
                return quote;
            }).collect(Collectors.toList());
            userBook.setQuotes(quotes);
        } else {
            userBook.setQuotes(new ArrayList<>());
        }
        
        // 4. 저장
        UserBookEntity saved = userBookRepository.save(userBook);
        
        // 5. 저장된 엔티티를 DTO로 변환하여 반환
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(saved, UserBookDto.class);
    }

    @Override
    public List<UserBookDto> getUserBooks(Long userId) {
        // userId로 사용자 도서 목록 조회 (리포지토리 메서드 이름: findByUser_Id)
        List<UserBookEntity> userBooks = userBookRepository.findByUser_Id(userId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return userBooks.stream()
                .map(book -> mapper.map(book, UserBookDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserBookDto getUserBook(Long userId, Long bookId) {
        // userId와 bookId로 단건 조회 (리포지토리 메서드 이름: findByIdAndUser_Id)
        Optional<UserBookEntity> optionalBook = userBookRepository.findByIdAndUser_Id(bookId, userId);
        if (optionalBook.isPresent()) {
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return mapper.map(optionalBook.get(), UserBookDto.class);
        }
        return null;
    }

    @Override
    public boolean deleteUserBook(Long userId, Long bookId) {
        Optional<UserBookEntity> optionalBook = userBookRepository.findByIdAndUser_Id(bookId, userId);
        if (optionalBook.isPresent()) {
            userBookRepository.delete(optionalBook.get());
            return true;
        }
        return false;
    }

    @Override
    public UserBookDto updateUserBook(UserBookDto userBookDto) {
        // 기존 도서 정보를 userId와 id로 조회
        Optional<UserBookEntity> optionalBook = userBookRepository.findByIdAndUser_Id(userBookDto.getId(), userBookDto.getUserId());
        if (!optionalBook.isPresent()) {
            return null;
        }
        UserBookEntity userBook = optionalBook.get();
        
        // 필드 업데이트 (필요에 따라 부분 업데이트 로직 추가)
        userBook.setIsbn(userBookDto.getIsbn());
        userBook.setTitle(userBookDto.getTitle());
        userBook.setAuthor(userBookDto.getAuthor());
        userBook.setPublisher(userBookDto.getPublisher());
        userBook.setPDate(userBookDto.getPDate());
        userBook.setDescription(userBookDto.getDescription());
        userBook.setThumbnail(userBookDto.getThumbnail());
        userBook.setPersonalReview(userBookDto.getPersonalReview());
        userBook.setStartDate(userBookDto.getStartDate());
        userBook.setEndDate(userBookDto.getEndDate());
        
        // 기존 인용구 삭제 후 업데이트
        userBook.getQuotes().clear();
        if (userBookDto.getQuotes() != null) {
            List<BookQuoteEntity> quotes = userBookDto.getQuotes().stream().map(dto -> {
                BookQuoteEntity quote = new BookQuoteEntity();
                quote.setPageNumber(dto.getPageNumber());
                quote.setQuoteText(dto.getQuoteText());
                quote.setUserBook(userBook);
                return quote;
            }).collect(Collectors.toList());
            userBook.getQuotes().addAll(quotes);
        }
        
        UserBookEntity saved = userBookRepository.save(userBook);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(saved, UserBookDto.class);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        // username으로 User 엔티티 조회
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null ? user.getId() : null;
    }
}
