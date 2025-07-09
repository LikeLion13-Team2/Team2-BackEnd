package com.poco.poco_backend.domain.report.controller;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.exception.MemberErrorCode;
import com.poco.poco_backend.domain.member.exception.MemberException;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.domain.report.dto.request.ReportRequestDTO;
import com.poco.poco_backend.domain.report.dto.response.ReportResponseDTO;
import com.poco.poco_backend.domain.report.service.command.ReportCommandService;
import com.poco.poco_backend.global.CustomResponse;
import com.poco.poco_backend.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportCommandService reportCommandService;
    private final MemberRepository memberRepository;

    @PostMapping
    public CustomResponse<ReportResponseDTO.CreateReportDTO> createReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReportRequestDTO.CreateReportDTO request
            ) {

        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return CustomResponse.onSuccess(reportCommandService.createReport(member, request));

    }
}
