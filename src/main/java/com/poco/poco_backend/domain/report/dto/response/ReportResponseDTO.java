package com.poco.poco_backend.domain.report.dto.response;

import lombok.Builder;

public class ReportResponseDTO {

    @Builder
    public record ReportDetailResponseDTO(
            Long reportId,
            Long totalSessionSeconds,
            Long totalFocusSeconds,
            Long totalDistractionSeconds,
            Long totalBreakSeconds,
            Long longestFocusSeconds,
            Double avgFocusScore,
            String comment
    ) {
    }
}
