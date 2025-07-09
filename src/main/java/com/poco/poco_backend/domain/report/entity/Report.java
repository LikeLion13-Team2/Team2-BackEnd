package com.poco.poco_backend.domain.report.entity;

import com.poco.poco_backend.common.entity.BaseTimeEntity;
import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;
    private LocalDate baseDate;

    private Long totalSessionSeconds;
    private Long totalFocusSeconds;
    private Long totalDistractionSeconds;
    private Long totalBreakSeconds;
    private Long longestFocusSeconds;
    private Double avgFocusScore;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportSession> reportSessions = new ArrayList<>();

    public Report(Member member, PeriodType periodType, LocalDate baseDate) {
        this.member = member;
        this.periodType = periodType;
        this.baseDate = baseDate;
        this.reportSessions = new ArrayList<>();
    }

    public void addReportSession(StudySession session) {
        ReportSession reportSession = new ReportSession(this, session);
        this.reportSessions.add(reportSession);
    }

    public void clearReportSessions() {
        this.reportSessions.clear();
    }

    public void updateStats(
            Long totalSessionSeconds,
            Long totalFocusSeconds,
            Long totalDistractionSeconds,
            Long totalBreakSeconds,
            Long longestFocusSeconds,
            Double avgFocusScore
    ) {
        this.totalSessionSeconds = totalSessionSeconds;
        this.totalFocusSeconds = totalFocusSeconds;
        this.totalDistractionSeconds = totalDistractionSeconds;
        this.totalBreakSeconds = totalBreakSeconds;
        this.longestFocusSeconds = longestFocusSeconds;
        this.avgFocusScore = avgFocusScore;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

}
