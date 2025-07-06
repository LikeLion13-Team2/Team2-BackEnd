package com.poco.poco_backend.domain.studySession.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

public class StudySessionResponseDTO {

    @Builder
    public record CreateStudySessionResponseDTO(
            Long sessionId,
            String title,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            Long sessionSeconds,
            Long focusSeconds,
            Long distractionSeconds,
            Long breakSeconds,
            Long longestFocusSeconds,
            Double focusScore
    ) {
    }

}
