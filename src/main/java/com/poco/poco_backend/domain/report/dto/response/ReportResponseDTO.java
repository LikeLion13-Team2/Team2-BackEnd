package com.poco.poco_backend.domain.report.dto.response;

import com.poco.poco_backend.common.enums.PeriodType;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDate;

public class ReportResponseDTO {

    @Builder
    public record CreateReportDTO(
            Long reportId,
            PeriodType periodType,
            LocalDate baseDate,
            Integer avgFocusScore,
            Long totalStudyTime,
            Long totalFocusTime,
            Long totalBreakTime,
            Long totalDistractionTime,
            Long maxFocusTime,
            String comment
    ) {
    }

    @Builder
    public record ReportData(
            int avgScore,
            Duration totalFocusTime,
            Duration breakTime,
            Duration distractionTime,
            Duration maxFocusTime,
            int sessionCount
    ) {
    }
}
