package com.foco.foco_backend.domain.report.service.command;

import com.foco.foco_backend.common.enums.PeriodType;
import com.foco.foco_backend.domain.member.entity.Member;

import java.time.LocalDate;

public interface ReportCommandService {

    void updateOrCreateReport(Member member, PeriodType periodType, LocalDate baseDate);
}
