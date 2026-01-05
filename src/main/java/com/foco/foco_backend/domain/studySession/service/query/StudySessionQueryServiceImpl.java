package com.foco.foco_backend.domain.studySession.service.query;

import com.foco.foco_backend.domain.studySession.converter.StudySessionConverter;
import com.foco.foco_backend.domain.studySession.dto.response.StudySessionResponseDTO;
import com.foco.foco_backend.domain.studySession.entity.StudySession;
import com.foco.foco_backend.domain.studySession.exception.StudySessionErrorCode;
import com.foco.foco_backend.domain.studySession.exception.StudySessionException;
import com.foco.foco_backend.domain.studySession.repostitory.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudySessionQueryServiceImpl implements StudySessionQueryService {

    private final StudySessionRepository studySessionRepository;

    @Override
    public StudySessionResponseDTO.StudySessionDetailResponseDTO getStudySessionDetail(Long studySessionId, String email) {
        StudySession studySession = studySessionRepository.findById(studySessionId)
                .orElseThrow(() -> new StudySessionException(StudySessionErrorCode.SESSION_NOT_FOUND));

        if (!studySession.getMember().getEmail().equals(email)) {
            throw new StudySessionException(StudySessionErrorCode.UNAUTHORIZED_ACCESS);
        }

        return StudySessionConverter.toStudySessionDetailResponseDTO(studySession);
    }

    @Override
    public List<StudySessionResponseDTO.StudySessionDetailResponseDTO> getStudySessions(String email) {
        List<StudySession> sessions = studySessionRepository.findAllByMemberEmail(email);

        return sessions.stream()
                .map(StudySessionConverter::toStudySessionDetailResponseDTO)
                .collect(Collectors.toList());
    }

}
