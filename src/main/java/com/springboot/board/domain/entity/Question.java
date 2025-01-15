package com.springboot.board.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import com.springboot.board.common.util.DateTimeUtil;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder(toBuilder = true) // 빌더를 다시 생성할 때 모든 필드 포함
@AllArgsConstructor // 필드 매핑 문제 해결
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answers;

    @Column(length = 100) // 작성자 이름의 최대 길이를 100자로 설정
    private String author; // 작성자

    @Column(columnDefinition = "int default 0")
    private int viewCount = 0; // 기본값을 0으로 설정

    // Question.java
    @Column(nullable = false, length = 200)
    private String password;

    @Builder
    public Question(String subject, String content, String author, String password) {
        this.subject = subject;
        this.content = content;
        this.author = author; // 작성자 추가
        this.viewCount = 0; // 기본값 0으로 초기화
        this.createDate = DateTimeUtil.now();
        this.password = password;
    }

    // createDate를 자동으로 설정하는 메서드
    @PrePersist
    public void prePersist() {
        this.createDate = this.createDate == null ? LocalDateTime.now() : this.createDate;
    }

    // 조회수 증가 메서드 추가
    public void incrementViewCount() {
        this.viewCount++;
    }
}
