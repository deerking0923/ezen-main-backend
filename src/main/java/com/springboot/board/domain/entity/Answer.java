package com.springboot.board.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.AccessLevel;
import com.springboot.board.common.util.DateTimeUtil;

@Getter
@Setter
@Entity
// 기본 생성자를 protected로 설정하여 외부에서 직접 인스턴스를 생성하지 못하도록 함
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 답변의 고유 ID

    @Column(columnDefinition = "TEXT")
    private String content; // 답변의 내용

    private LocalDateTime createDate;

    // 질문과의 다대일 관계를 설정하며, 지연 로딩을 사용하여 성능을 최적화하고 불필요한 데이터 로딩을 방지함
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    // Builder 패턴을 사용하여 답변 객체를 생성하기 위한 생성자
    @Builder
    public Answer(String content, Question question) {
        this.content = content;
        this.question = question;
        this.createDate = DateTimeUtil.now();
    }
}