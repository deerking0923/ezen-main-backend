package com.springboot.board.api.v1.dto.response;

import com.springboot.board.api.v1.dto.BookQuoteDto;
import lombok.Data;
import java.util.List;

@Data
public class ResponseUserBook {
    private Long id;
    private Long userId;
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
