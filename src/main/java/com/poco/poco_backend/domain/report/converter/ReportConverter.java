package com.poco.poco_backend.domain.report.converter;

import com.poco.poco_backend.domain.report.dto.request.ReportRequestDTO;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.entity.Report;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportConverter {

    public static ReportResponseDTO.ReportDetailResponseDTO toReportDetailResponseDTO(Report report) {
        return ReportResponseDTO.ReportDetailResponseDTO.builder()
                .reportId(report.getId())
                .avgFocusScore(report.getAvgFocusScore())
                .totalSessionSeconds(report.getTotalSessionSeconds())
                .totalFocusSeconds(report.getTotalFocusSeconds())
                .totalDistractionSeconds(report.getTotalDistractionSeconds())
                .totalBreakSeconds(report.getTotalBreakSeconds())
                .longestFocusSeconds(report.getLongestFocusSeconds())
                .comment(report.getComment())
                .build();
    }

    public static ReportRequestDTO.ReportData toReportData(List<StudySession> sessions) {
        long totalSession = 0L;
        long totalFocus = 0L;
        long totalDistraction = 0L;
        long totalBreak = 0L;
        long longestFocus = 0L;
        double focusScoreSum = 0.0;

        for (StudySession session : sessions) {
            totalSession += session.getSessionSeconds();
            totalFocus += session.getFocusSeconds();
            totalDistraction += session.getDistractionSeconds();
            totalBreak += session.getBreakSeconds();
            longestFocus = Math.max(longestFocus, session.getLongestFocusSeconds());
            focusScoreSum += session.getFocusScore();
        }

        double avgFocusScore = sessions.isEmpty() ? 0.0 : focusScoreSum / sessions.size();

        return ReportRequestDTO.ReportData.builder()
                .avgFocusScore(avgFocusScore)
                .totalSessionSeconds(totalSession)
                .totalFocusSeconds(totalFocus)
                .totalDistractionSeconds(totalDistraction)
                .totalBreakSeconds(totalBreak)
                .longestFocusSeconds(longestFocus)
                .build();
    }
}
