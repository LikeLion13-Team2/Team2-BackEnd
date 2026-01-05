package com.foco.foco_backend.domain.studySession.service.query;

import com.foco.foco_backend.domain.studySession.dto.response.StudySessionResponseDTO;

import java.util.List;

public interface StudySessionQueryService {

    StudySessionResponseDTO.StudySessionDetailResponseDTO getStudySessionDetail(Long studySessionId, String email);

    List<StudySessionResponseDTO.StudySessionDetailResponseDTO> getStudySessions(String email);

}
