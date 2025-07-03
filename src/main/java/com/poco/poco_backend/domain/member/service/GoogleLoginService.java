package com.poco.poco_backend.domain.member.service;

import com.poco.poco_backend.global.security.jwt.JwtDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleLoginService {

    //private final JwtUtil jwtUtil;
    //private final TokenRepository tokenRepository;

    public JwtDTO reissueToken(JwtDTO jwtDto) {

        log.info("[ Auth Service ] 토큰 재발급을 시작합니다.");
        String accessToken = jwtDto.jwtAccessToken();
        String refreshToken = jwtDto.jwtRefreshToken();

        //Access Token 으로부터 사용자 Email 추출


    }
}
