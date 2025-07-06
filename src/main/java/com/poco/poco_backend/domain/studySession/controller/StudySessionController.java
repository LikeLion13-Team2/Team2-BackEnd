package com.poco.poco_backend.domain.studySession.controller;

import com.poco.poco_backend.domain.studySession.dto.request.StudySessionRequestDTO;
import com.poco.poco_backend.domain.studySession.dto.response.StudySessionResponseDTO;
import com.poco.poco_backend.domain.studySession.service.command.StudySessionCommandService;
import com.poco.poco_backend.global.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study-sessions")
@Tag(name = "StudySession", description = "학습 세션 관련 API by 박현빈")
public class StudySessionController {

    private final StudySessionCommandService studySessionCommandService;

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
            @RequestBody StudySessionRequestDTO.CreateStudySessionRequestDTO request
            // TODO: @AuthenticationPrincipal 추가
    ) {
        return CustomResponse.onSuccess(studySessionCommandService.createSession(request));
    }

}
