package com.springboot.board.application.service;

import com.springboot.board.api.v1.dto.BookQuoteDto;
import com.springboot.board.api.v1.dto.UserBookDto;
import com.springboot.board.domain.entity.BookQuoteEntity;
import com.springboot.board.domain.entity.User;
import com.springboot.board.domain.entity.UserBookEntity;
import com.springboot.board.domain.repository.BookQuoteRepository;
import com.springboot.board.domain.repository.UserBookRepository;
import com.springboot.board.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MyLibraryServiceImpl implements MyLibraryService {

    private final UserBookRepository userBookRepository;
    private final BookQuoteRepository bookQuoteRepository;
    private final UserRepository userRepository;

    public MyLibraryServiceImpl(UserBookRepository userBookRepository,
                                BookQuoteRepository bookQuoteRepository,
                                UserRepository userRepository) {
        this.userBookRepository = userBookRepository;
        this.bookQuoteRepository = bookQuoteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserBookDto createUserBook(UserBookDto userBookDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // User 엔티티 조회
        Optional<User> optionalUser = userRepository.findById(userBookDto.getUserId());
        if (optionalUser.isEmpty()) {
            // 없으면 null 반환하거나 예외 처리
            return null;
        }
        User user = optionalUser.get();

        // DTO → Entity 매핑
        UserBookEntity userBookEntity = mapper.map(userBookDto, UserBookEntity.class);
        // User 엔티티와 연관관계 설정
        userBookEntity.setUser(user);

        if (userBookDto.getQuotes() != null && !userBookDto.getQuotes().isEmpty()) {
            List<BookQuoteEntity> quoteEntities = new ArrayList<>();
            for (BookQuoteDto quoteDto : userBookDto.getQuotes()) {
                BookQuoteEntity quoteEntity = new BookQuoteEntity();
                quoteEntity.setPageNumber(quoteDto.getPageNumber());
                quoteEntity.setQuoteText(quoteDto.getQuoteText());
                quoteEntity.setUserBook(userBookEntity);
                quoteEntities.add(quoteEntity);
            }
            userBookEntity.setQuotes(quoteEntities);
        }

        userBookRepository.save(userBookEntity);
        return mapper.map(userBookEntity, UserBookDto.class);
    }

    @Override
    public List<UserBookDto> getUserBooks(Long userId) {
        List<UserBookEntity> entities = userBookRepository.findByUserId(userId);
        List<UserBookDto> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        for (UserBookEntity entity : entities) {
            UserBookDto dto = mapper.map(entity, UserBookDto.class);
            List<BookQuoteEntity> quoteEntities = bookQuoteRepository.findByUserBook_Id(entity.getId());
            List<BookQuoteDto> quoteDtos = new ArrayList<>();
            for (BookQuoteEntity qe : quoteEntities) {
                BookQuoteDto quoteDto = new BookQuoteDto();
                quoteDto.setId(qe.getId());
                quoteDto.setPageNumber(qe.getPageNumber());
                quoteDto.setQuoteText(qe.getQuoteText());
                quoteDtos.add(quoteDto);
            }
            dto.setQuotes(quoteDtos);
            result.add(dto);
        }
        return result;
    }

    @Override
    public UserBookDto getUserBook(Long userId, Long bookId) {
        UserBookEntity userBookEntity = userBookRepository.findById(bookId).orElse(null);
        if (userBookEntity == null || !userBookEntity.getUser().getId().equals(userId)) {
            return null;
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserBookDto userBookDto = mapper.map(userBookEntity, UserBookDto.class);
        List<BookQuoteEntity> quoteEntities = bookQuoteRepository.findByUserBook_Id(bookId);
        List<BookQuoteDto> quoteDtos = new ArrayList<>();
        for (BookQuoteEntity qe : quoteEntities) {
            BookQuoteDto quoteDto = new BookQuoteDto();
            quoteDto.setId(qe.getId());
            quoteDto.setPageNumber(qe.getPageNumber());
            quoteDto.setQuoteText(qe.getQuoteText());
            quoteDtos.add(quoteDto);
        }
        userBookDto.setQuotes(quoteDtos);
        return userBookDto;
    }

    @Override
    public boolean deleteUserBook(Long userId, Long bookId) {
        Optional<UserBookEntity> optionalEntity = userBookRepository.findById(bookId);
        if (optionalEntity.isEmpty() || !optionalEntity.get().getUser().getId().equals(userId)) {
            return false;
        }
        try {
            userBookRepository.delete(optionalEntity.get());
            return true;
        } catch (Exception e) {
            log.error("Error deleting user book", e);
            return false;
        }
    }

    @Override
    public UserBookDto updateUserBook(UserBookDto userBookDto) {
        Optional<UserBookEntity> optionalEntity = userBookRepository.findById(userBookDto.getId());
        if (optionalEntity.isEmpty() || !optionalEntity.get().getUser().getId().equals(userBookDto.getUserId())) {
            return null;
        }
        UserBookEntity existingEntity = optionalEntity.get();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        existingEntity.setIsbn(userBookDto.getIsbn());
        existingEntity.setTitle(userBookDto.getTitle());
        existingEntity.setAuthor(userBookDto.getAuthor());
        existingEntity.setPublisher(userBookDto.getPublisher());
        existingEntity.setPDate(userBookDto.getPDate());
        existingEntity.setDescription(userBookDto.getDescription());
        existingEntity.setThumbnail(userBookDto.getThumbnail());
        existingEntity.setPersonalReview(userBookDto.getPersonalReview());
        existingEntity.setStartDate(userBookDto.getStartDate());
        existingEntity.setEndDate(userBookDto.getEndDate());

        if (userBookDto.getQuotes() != null) {
            if (existingEntity.getQuotes() != null) {
                existingEntity.getQuotes().clear();
            } else {
                existingEntity.setQuotes(new ArrayList<>());
            }
            for (BookQuoteDto quoteDto : userBookDto.getQuotes()) {
                BookQuoteEntity quoteEntity = new BookQuoteEntity();
                quoteEntity.setPageNumber(quoteDto.getPageNumber());
                quoteEntity.setQuoteText(quoteDto.getQuoteText());
                quoteEntity.setUserBook(existingEntity);
                existingEntity.getQuotes().add(quoteEntity);
            }
        }

        userBookRepository.save(existingEntity);
        return mapper.map(existingEntity, UserBookDto.class);
    }
}
