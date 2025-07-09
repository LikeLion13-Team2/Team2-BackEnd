package com.poco.poco_backend.domain.report.service.query;

import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;

import java.time.LocalDate;

public interface ReportQueryService {

    ReportResponseDTO.ReportDetailResponseDTO getReport(
            Member member, PeriodType periodType, LocalDate baseDate);
}
