package com.poco.poco_backend.domain.report.entity;

import com.poco.poco_backend.common.entity.BaseTimeEntity;
import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    private LocalDate baseDate;
    private Double avgFocusScore;
    private LocalDateTime focusPeakTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportSession> reportSessions = new ArrayList<>();
}
