package com.poco.poco_backend.domain.report.service.command;

import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.converter.ReportConverter;
import com.poco.poco_backend.domain.report.entity.Report;
import com.poco.poco_backend.domain.report.repository.ReportRepository;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import com.poco.poco_backend.domain.studySession.exception.StudySessionErrorCode;
import com.poco.poco_backend.domain.studySession.exception.StudySessionException;
import com.poco.poco_backend.domain.studySession.repostitory.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportCommandServiceImpl {
@RequiredArgsConstructor
@Transactional
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final StudySessionRepository studySessionRepository;

    public void updateOrCreateReport(Member member, PeriodType periodType, LocalDate baseDate) {

        // 기간 설정
        int days = periodType.getDays();
        LocalDateTime start = baseDate.minusDays(days - 1).atStartOfDay();
        LocalDateTime end = baseDate.plusDays(1).atStartOfDay();

        // 해당 기간 StudySession 조회
        List<StudySession> sessions = studySessionRepository
                .findByMemberAndStartedAtBetween(member, start, end);

        if (sessions.isEmpty()) {
            throw new StudySessionException(StudySessionErrorCode.SESSION_NOT_FOUND);
        }

        // 기존 리포트 존재 확인
        Report report = reportRepository.findByMemberAndPeriodTypeAndBaseDate(
                        member, periodType, baseDate)
                .orElseGet(() -> new Report(member, periodType, baseDate));

        // ReportSession 갱신
        report.clearReportSessions();
        for (StudySession session : sessions) {
            report.addReportSession(session);
        }

        // 통계 계산
        long totalSessionSeconds = sessions.stream().mapToLong(StudySession::getSessionSeconds).sum();
        long totalFocusSeconds = sessions.stream().mapToLong(StudySession::getFocusSeconds).sum();
        long totalDistractionSeconds = sessions.stream().mapToLong(StudySession::getDistractionSeconds).sum();
        long totalBreakSeconds = sessions.stream().mapToLong(StudySession::getBreakSeconds).sum();
        long longestFocusSeconds = sessions.stream().mapToLong(StudySession::getLongestFocusSeconds).max().orElse(0);
        double avgFocusScore = sessions.stream().mapToDouble(StudySession::getFocusScore).average().orElse(0);

        report.updateStats(
                totalSessionSeconds,
                totalFocusSeconds,
                totalDistractionSeconds,
                totalBreakSeconds,
                longestFocusSeconds,
                avgFocusScore
        );

        reportRepository.save(report);

    }

}
