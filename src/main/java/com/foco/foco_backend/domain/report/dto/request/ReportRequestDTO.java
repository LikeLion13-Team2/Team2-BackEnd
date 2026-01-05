package com.foco.foco_backend.domain.report.dto.request;

import lombok.Builder;

public class ReportRequestDTO {

    @Builder
    public record ReportData(
            Double avgFocusScore,
            Long totalSessionSeconds,
            Long totalFocusSeconds,
            Long totalDistractionSeconds,
            Long totalBreakSeconds,
            Long longestFocusSeconds
    ) {
    }
}
