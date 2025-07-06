package com.poco.poco_backend.domain.studySession.service.command;

import com.poco.poco_backend.domain.studySession.dto.request.StudySessionRequestDTO;
import com.poco.poco_backend.domain.studySession.dto.response.StudySessionResponseDTO;

public interface StudySessionCommandService {

    StudySessionResponseDTO.CreateStudySessionResponseDTO createSession(StudySessionRequestDTO.CreateStudySessionRequestDTO createDTO);

}
