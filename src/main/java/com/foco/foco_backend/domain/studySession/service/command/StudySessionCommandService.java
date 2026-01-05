package com.foco.foco_backend.domain.studySession.service.command;

import com.foco.foco_backend.domain.studySession.dto.request.StudySessionRequestDTO;
import com.foco.foco_backend.domain.studySession.dto.response.StudySessionResponseDTO;

public interface StudySessionCommandService {

    StudySessionResponseDTO.CreateStudySessionResponseDTO createSession(StudySessionRequestDTO.CreateStudySessionRequestDTO createDTO, String email);

}
