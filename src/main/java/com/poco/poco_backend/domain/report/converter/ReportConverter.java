package com.poco.poco_backend.domain.report.converter;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.dto.request.ReportRequestDTO;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.entity.Report;
import com.poco.poco_backend.domain.report.exception.ReportErrorCode;
import com.poco.poco_backend.domain.report.exception.ReportException;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportConverter {

    // AI 코멘트용
    public static ReportResponseDTO.ReportData toReportDataDTO(List<StudySession> sessions) {
        if (sessions.isEmpty()) {
            throw new ReportException(ReportErrorCode.SESSION_NOT_FOUND);
        }

        // 평균 집중 점수
        double avgScore = sessions.stream()
                .mapToDouble(StudySession::getFocusScore)
                .average()
                .orElse(0);

        // 총 시간
        long totalFocusSeconds = sessions.stream()
                .mapToLong(StudySession::getFocusSeconds)
                .sum();
        long totalBreakSeconds = sessions.stream()
                .mapToLong(StudySession::getBreakSeconds)
                .sum();
        long totalDistractionSeconds = sessions.stream()
                .mapToLong(StudySession::getLongestFocusSeconds)
                .max()
                .orElse(0);

        long maxFocusSeconds = sessions.stream()
                .mapToLong(StudySession::getLongestFocusSeconds)
                .max()
                .orElse(0);

        return ReportResponseDTO.ReportData.builder()
                .avgScore((int) Math.round(avgScore))
                .totalFocusTime(Duration.ofSeconds(totalFocusSeconds))
                .breakTime(Duration.ofSeconds(totalBreakSeconds))
                .distractionTime(Duration.ofSeconds(totalDistractionSeconds))
                .maxFocusTime(Duration.ofSeconds(maxFocusSeconds))
                .sessionCount(sessions.size())
                .build();
    }

    // Report 저장용
    public static ReportRequestDTO.ReportStats calculateStats(List<StudySession> sessions) {
        if (sessions.isEmpty()) {
            throw new ReportException(ReportErrorCode.SESSION_NOT_FOUND);
        }

        double avg = sessions.stream()
                .mapToDouble(StudySession::getFocusScore)
                .average()
                .orElse(0);

        long focus = sessions.stream()
                .mapToLong(StudySession::getFocusSeconds)
                .sum();

        long breakTime = sessions.stream()
                .mapToLong(StudySession::getBreakSeconds)
                .sum();

        long distraction = sessions.stream()
                .mapToLong(StudySession::getDistractionSeconds)
                .sum();

        long maxFocus = sessions.stream()
                .mapToLong(StudySession::getLongestFocusSeconds)
                .max()
                .orElse(0);

        long totalStudy = focus + breakTime;

        return ReportRequestDTO.ReportStats.builder()
                .avgFocusScore((int) Math.round(avg))
                .totalStudySeconds(totalStudy)
                .maxFocusSeconds(maxFocus)
                .totalBreakSeconds(breakTime)
                .totalDistractionSeconds(distraction)
                .totalFocusSeconds(focus)
                .build();
    }

    public static Report toEntity(
            ReportRequestDTO.CreateReportDTO request,
            ReportRequestDTO.ReportStats stats,
            Member member) {
        return Report.builder()
                .periodType(request.periodType())
                .baseDate(request.baseDate())
                .avgFocusScore(stats.avgFocusScore())
                .totalStudyTime(stats.totalStudySeconds())
                .totalBreakTime(stats.totalBreakSeconds())
                .totalDistractionTime(stats.totalDistractionSeconds())
                .totalFocusTime(stats.totalFocusSeconds())
                .maxFocusTime(stats.maxFocusSeconds())
                .member(member)
                .build();
    }

    public static ReportResponseDTO.CreateReportDTO toCreateReportResponseDTO(Report report) {
        return ReportResponseDTO.CreateReportDTO.builder()
                .reportId(report.getId())
                .periodType(report.getPeriodType())
                .baseDate(report.getBaseDate())
                .avgFocusScore(report.getAvgFocusScore())
                .comment(report.getComment())
                .totalFocusTime(report.getTotalFocusTime())
                .totalBreakTime(report.getTotalBreakTime())
                .totalStudyTime(report.getTotalStudyTime())
                .maxFocusTime(report.getMaxFocusTime())
                .totalDistractionTime(report.getTotalDistractionTime())
                .build();
    }
}
