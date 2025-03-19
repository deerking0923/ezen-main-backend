package com.springboot.board.api.v1.dto;

import lombok.Data;

@Data
public class BookQuoteDto {
    private Long id;
    private String pageNumber;
    private String quoteText;
}
