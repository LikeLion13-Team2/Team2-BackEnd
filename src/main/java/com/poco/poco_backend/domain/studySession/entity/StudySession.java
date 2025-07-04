package com.poco.poco_backend.domain.studySession.entity;

import com.poco.poco_backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long focusSeconds;
    private Long distractionSeconds;
    private Long breakSeconds;
    private Long maxFocusDurationSeconds;

    private LocalDateTime focusPeakTime;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
