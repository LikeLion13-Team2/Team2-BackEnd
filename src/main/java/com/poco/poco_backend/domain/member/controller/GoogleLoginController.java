package com.poco.poco_backend.domain.member.controller;

import com.poco.poco_backend.domain.member.service.GoogleLoginService;
import com.poco.poco_backend.global.CustomResponse;
import com.poco.poco_backend.global.security.jwt.JwtDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "구글 로그인 토큰 발급 api", description = "구글 로그인 토큰 발급 api입니다.")
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

    //토큰 재발급 api
    @Operation(method = "POST", summary = "토큰 재발급",
            description = "토큰 재발급. accessToken과 refreshToken을 body에 담아서 전송합니다.")
    @PostMapping("/reissue")
    public CustomResponse<?> reissue(JwtDTO jwtDto) throws SignatureException {

        log.info("[ Google Login Controller ] 토큰을 재발급 합니다.");

        return CustomResponse.onSuccess(googleLoginService.reissueToken(jwtDto));
    }
}
