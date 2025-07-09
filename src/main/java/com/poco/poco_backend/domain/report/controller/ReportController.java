package com.poco.poco_backend.domain.report.controller;

import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.exception.MemberErrorCode;
import com.poco.poco_backend.domain.member.exception.MemberException;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.service.query.ReportQueryService;
import com.poco.poco_backend.global.CustomResponse;
import com.poco.poco_backend.global.security.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Report", description = "리포트 관련 API by 박현빈")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportQueryService reportQueryService;
    private final MemberRepository memberRepository;

    @Operation(summary = "리포트 조회",
            description = """
    기간 타입(1일/3일/7일)과 기준 날짜를 기반으로 학습 리포트를 조회합니다.\n
    baseDate 예시: 2025-07-10
    """)
    @GetMapping
    public CustomResponse<ReportResponseDTO.ReportDetailResponseDTO> getReport(
            @RequestParam PeriodType periodType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return CustomResponse.onSuccess(reportQueryService.getReport(member, periodType, baseDate));
    }
}
