package com.poco.poco_backend.domain.report.service.query;

import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.converter.ReportConverter;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.entity.Report;
import com.poco.poco_backend.domain.report.exception.ReportErrorCode;
import com.poco.poco_backend.domain.report.exception.ReportException;
import com.poco.poco_backend.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportRepository reportRepository;

    public ReportResponseDTO.ReportDetailResponseDTO getReport(
            Member member, PeriodType periodType, LocalDate baseDate
    ) {
        Report report = reportRepository.findByMemberAndPeriodTypeAndBaseDate(
                        member, periodType, baseDate)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

        return ReportConverter.toReportDetailResponseDTO(report);
    }
}
