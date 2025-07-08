package com.poco.poco_backend.domain.member.controller;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.global.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.poco.poco_backend.global.CustomResponse.onSuccess;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
@Tag(name = "카카오 로그인 관련 api", description = "카카오 로그인 토큰 관련 api 입니다.")
public class KaKaoLoginController {

    @Operation(summary = "카카오 로그인 인가 및 토큰 발급"
            , description = "카카오 로그인 인가 및 토큰발급 api 입니다.")
    public CustomResponse<?> kakaoLogin(@RequestParam("code")String accessCode,
                                        HttpServletResponse httpServletResponse) {
        return null;
    }

}
