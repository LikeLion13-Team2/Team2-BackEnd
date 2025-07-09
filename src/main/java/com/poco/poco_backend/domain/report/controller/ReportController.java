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
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportQueryService reportQueryService;
    private final MemberRepository memberRepository;

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
