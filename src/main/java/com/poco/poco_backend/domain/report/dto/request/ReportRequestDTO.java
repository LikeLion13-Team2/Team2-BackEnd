package com.poco.poco_backend.domain.report.dto.request;

import com.poco.poco_backend.common.enums.PeriodType;
import lombok.Builder;

import java.time.LocalDate;

public class ReportRequestDTO {

    @Builder
    public record CreateReportDTO(
            PeriodType periodType,
            LocalDate baseDate
    ) {
    }

    @Builder
    public record ReportStats(
            int avgFocusScore,
            long totalStudySeconds,
            long totalFocusSeconds,
            long totalBreakSeconds,
            long totalDistractionSeconds,
            long maxFocusSeconds
    ) {
    }
}
