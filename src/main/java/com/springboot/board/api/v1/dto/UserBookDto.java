package com.springboot.board.api.v1.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserBookDto {
    private Long id;
    private Long userId; // 기존 문자열 대신 User 엔티티의 id (Long)
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;
    private List<BookQuoteDto> quotes;
    private String startDate;
    private String endDate;
}
