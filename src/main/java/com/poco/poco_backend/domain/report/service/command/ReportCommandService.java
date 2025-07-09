package com.poco.poco_backend.domain.report.service.command;

import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;

import java.time.LocalDate;

public interface ReportCommandService {

    void updateOrCreateReport(Member member, PeriodType periodType, LocalDate baseDate);
}
