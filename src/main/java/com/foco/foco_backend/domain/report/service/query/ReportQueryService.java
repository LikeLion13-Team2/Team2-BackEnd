package com.foco.foco_backend.domain.report.service.query;

import com.foco.foco_backend.common.enums.PeriodType;
import com.foco.foco_backend.domain.member.entity.Member;
import com.foco.foco_backend.domain.report.dto.response.ReportResponseDTO;

import java.time.LocalDate;

public interface ReportQueryService {

    ReportResponseDTO.ReportDetailResponseDTO getReport(
            Member member, PeriodType periodType, LocalDate baseDate);
}
