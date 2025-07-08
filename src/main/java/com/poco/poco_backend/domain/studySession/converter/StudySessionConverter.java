package com.poco.poco_backend.domain.studySession.converter;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.studySession.dto.request.StudySessionRequestDTO;
import com.poco.poco_backend.domain.studySession.dto.response.StudySessionResponseDTO;
import com.poco.poco_backend.domain.studySession.entity.StudySession;

public class StudySessionConverter {

    public static StudySession toStudySession(StudySessionRequestDTO.CreateStudySessionRequestDTO dto,
                                              StudySessionRequestDTO.StudySessionCalculations calculations,
                                              Member member) {
        return StudySession.builder()
                .title(dto.title())
                .startedAt(dto.startedAt())
                .endedAt(dto.endedAt())
                .sessionSeconds(calculations.sessionSeconds())
                .focusSeconds(calculations.focusSeconds())
                .distractionSeconds(calculations.distractionSeconds())
                .breakSeconds(calculations.breakSeconds())
                .longestFocusSeconds(calculations.longestFocusSeconds())
                .focusScore(dto.focusScore())
                .member(member)
                .build();
    }

    public static StudySessionResponseDTO.CreateStudySessionResponseDTO toStudySessionResponseDTO(StudySession studySession) {
        return StudySessionResponseDTO.CreateStudySessionResponseDTO.builder()
                .sessionId(studySession.getId())
                .title(studySession.getTitle())
                .startedAt(studySession.getStartedAt())
                .endedAt(studySession.getEndedAt())
                .sessionSeconds(studySession.getSessionSeconds())
                .focusSeconds(studySession.getFocusSeconds())
                .distractionSeconds(studySession.getDistractionSeconds())
                .breakSeconds(studySession.getBreakSeconds())
                .longestFocusSeconds(studySession.getLongestFocusSeconds())
                .focusScore(studySession.getFocusScore())
                .build();
    }
}
