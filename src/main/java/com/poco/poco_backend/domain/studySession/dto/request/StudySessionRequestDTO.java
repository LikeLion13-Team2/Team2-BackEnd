package com.poco.poco_backend.domain.studySession.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class StudySessionRequestDTO {

    @Builder
    public record CreateStudySessionRequestDTO(
            String title,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            List<DistractionLog> distractionLogs,
            List<BreakLog> breakLogs,
            Double focusScore
    ) {
        public record DistractionLog(
                LocalDateTime start,
                LocalDateTime end
        ) {
        }

        public record BreakLog(
                LocalDateTime start,
                LocalDateTime end
        ) {
        }
    }

    @Builder
    public record StudySessionCalculations(
            long sessionSeconds,
            long focusSeconds,
            long distractionSeconds,
            long breakSeconds,
            long longestFocusSeconds
    ) {
    }
}
