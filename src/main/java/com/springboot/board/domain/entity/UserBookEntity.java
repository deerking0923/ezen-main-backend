package com.springboot.board.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "user_books")
public class UserBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기존 userId (문자열) 대신 User 엔티티와 연관관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String isbn;

    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;

    @OneToMany(mappedBy = "userBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookQuoteEntity> quotes;

    private String startDate;
    private String endDate;
}
