package com.poco.poco_backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Google Login", description = "구글 로그인 관련 api입니다.")
public class GoogleLoginController {

    @Operation(summary = "구글 로그인", description = "구글 로그인 페이지로 리다이렉트 합니다.")
    @GetMapping("/oauth/authorization/google")
    public void googleLoginRedirect() {}
}
