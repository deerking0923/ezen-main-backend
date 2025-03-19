package com.springboot.board.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String subject;

    @Column(nullable=false, columnDefinition="TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy="question", cascade=CascadeType.REMOVE)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable=false)
    @Builder.Default
    private Integer viewCount = 0;

    public void incrementViewCount() {
        this.viewCount++;
    }
}
