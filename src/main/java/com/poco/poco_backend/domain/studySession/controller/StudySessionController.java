package com.poco.poco_backend.domain.studySession.controller;

import com.poco.poco_backend.domain.studySession.dto.request.StudySessionRequestDTO;
import com.poco.poco_backend.domain.studySession.dto.response.StudySessionResponseDTO;
import com.poco.poco_backend.domain.studySession.service.command.StudySessionCommandService;
import com.poco.poco_backend.domain.studySession.service.query.StudySessionQueryService;
import com.poco.poco_backend.global.CustomResponse;
import com.poco.poco_backend.global.security.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study-sessions")
@Tag(name = "StudySession", description = "학습 세션 관련 API by 박현빈")
public class StudySessionController {

    private final StudySessionCommandService studySessionCommandService;
    private final StudySessionQueryService studySessionQueryService;

    @Operation(
            summary = "학습 세션 저장",
            description = """
                    사용자의 한 세션 동안의 집중/딴짓/휴식 기록을 저장합니다.\n
                    프론트에서 타이머/딴짓/휴식의 시작/종료 시각을 타임스탬프 로그로 보내주면\n
                    백엔드에서 집중 통계를 계산하여 저장하고 반환합니다.
                    """
    )
    @PostMapping
    public CustomResponse<StudySessionResponseDTO.CreateStudySessionResponseDTO> createSession(
            @RequestBody StudySessionRequestDTO.CreateStudySessionRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return CustomResponse.onSuccess(studySessionCommandService.createSession(request, userDetails.getUsername()));
    }

    @Operation(
            summary = "학습 세션 조회",
            description = """
                    저장된 학습 세션을 조회합니다.\n
                    세션 제목, 시작/종료 시각, 세션 전체 시간, 집중 시간, 딴짓 시간, 쉬는 시간, 최장 집중 시간, 집중 점수를 조회할 수 있습니다.
                    """
    )
    @GetMapping("/{sessionId}")
    public CustomResponse<StudySessionResponseDTO.StudySessionDetailResponseDTO> getStudySession(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return CustomResponse.onSuccess(HttpStatus.OK, studySessionQueryService.getStudySessionDetail(sessionId, userDetails.getUsername()));
    }

    @Operation(
            summary = "학습 세션 리스트 조회",
            description = """
                    저장된 학습 세션 전체를 리스트로 조회합니다.\n
                    세션 제목, 시작/종료 시각, 세션 전체 시간, 집중 시간, 딴짓 시간, 쉬는 시간, 최장 집중 시간, 집중 점수를 조회할 수 있습니다.
                    """
    )
    @GetMapping
    public CustomResponse<List<StudySessionResponseDTO.StudySessionDetailResponseDTO>> getStudySessions(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return CustomResponse.onSuccess(HttpStatus.OK, studySessionQueryService.getStudySessions(userDetails.getUsername()));
    }

}
