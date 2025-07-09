package com.poco.poco_backend.domain.report.service.command;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.converter.ReportConverter;
import com.poco.poco_backend.domain.report.dto.request.ReportRequestDTO;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.entity.Report;
import com.poco.poco_backend.domain.report.repository.ReportRepository;
import com.poco.poco_backend.domain.report.service.query.ReportCommentService;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import com.poco.poco_backend.domain.studySession.repostitory.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportCommandServiceImpl implements ReportCommandService {

    private final StudySessionRepository studySessionRepository;
    private final ReportCommentService reportCommentService;
    private final ReportRepository reportRepository;

    public ReportResponseDTO.CreateReportDTO createReport(Member member, ReportRequestDTO.CreateReportDTO request) {

        LocalDate baseDate = request.baseDate();
        LocalDateTime startOfDay = baseDate.atStartOfDay();
        LocalDateTime endOfDay = baseDate.plusDays(1).atStartOfDay().minusNanos(1);


        // 기준 날짜와 기간 타입으로 세션 조회
        List<StudySession> sessions = studySessionRepository.findByMemberAndPeriodTypeAndStartedAtBetween(
                member, request.periodType(), startOfDay, endOfDay
        );

        System.out.println("💡 baseDate: " + baseDate);
        System.out.println("🕐 startOfDay: " + startOfDay);
        System.out.println("🕐 endOfDay: " + endOfDay);
        System.out.println("🧑‍🎓 memberId: " + member.getMemberId());
        System.out.println("📅 periodType: " + request.periodType());

        // 통계 계산
        ReportRequestDTO.ReportStats stats = ReportConverter.calculateStats(sessions);

        // 리포트 생성
        Report report = ReportConverter.toEntity(request, stats, member);

        ReportResponseDTO.ReportData aiData = ReportConverter.toReportDataDTO(sessions);
        String comment = reportCommentService.generateComment(aiData);
        report.writeComment(comment);

        reportRepository.save(report);

        return ReportConverter.toCreateReportResponseDTO(report);

    }

}
