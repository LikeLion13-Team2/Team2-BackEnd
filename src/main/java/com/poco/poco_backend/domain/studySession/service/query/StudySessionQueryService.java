package com.poco.poco_backend.domain.studySession.service.query;

import com.poco.poco_backend.domain.studySession.dto.response.StudySessionResponseDTO;

public interface StudySessionQueryService {

    StudySessionResponseDTO.StudySessionDetailResponseDTO getStudySessionDetail(Long studySessionId, String email);

}
