package com.springboot.board.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.board.api.v1.dto.BookQuoteDto;
import lombok.Data;
import java.util.List;

@Data
public class RequestUserBook {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    
    // JSON에서 "pDate"로 들어오는 값을 매핑하도록 어노테이션 추가
    @JsonProperty("pDate")
    private String pDate;
    
    private String description;
    private String thumbnail;
    private String personalReview;
    private List<BookQuoteDto> quotes;
    private String startDate;
    private String endDate;
}
