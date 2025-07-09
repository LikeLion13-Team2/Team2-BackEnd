package com.poco.poco_backend.domain.report.entity;

import com.poco.poco_backend.common.entity.BaseTimeEntity;
import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;
    private LocalDate baseDate;

    private Integer avgFocusScore;
    private Long totalStudyTime;
    private Long totalFocusTime;
    private Long totalBreakTime;
    private Long totalDistractionTime;
    private Long maxFocusTime;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportSession> reportSessions = new ArrayList<>();

    public void writeComment(String comment) {
        this.comment = comment;
    }
}
