package com.poco.poco_backend.domain.report.service.query;

import com.poco.poco_backend.domain.report.converter.ReportConverter;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.repository.ReportSessionRepository;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportSessionRepository reportSessionRepository;

    public ReportResponseDTO.ReportData getReport(Long reportId) {

        // 리포트에 연결된 세션 가져오기
        List<StudySession> sessions = reportSessionRepository.findAllStudySessionsByReportId(reportId);

        return ReportConverter.toReportDataDTO(sessions);

    }
}
