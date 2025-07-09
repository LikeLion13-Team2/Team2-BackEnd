package com.poco.poco_backend.domain.report.service.command;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.dto.request.ReportRequestDTO;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;

public interface ReportCommandService {

    ReportResponseDTO.CreateReportDTO createReport(Member member, ReportRequestDTO.CreateReportDTO request);
}
