package com.poco.poco_backend.domain.studySession.entity;

import com.poco.poco_backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long sessionSeconds;
    private Long focusSeconds;
    private Long distractionSeconds;
    private Long breakSeconds;
    private Long longestFocusSeconds;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private Double focusScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
