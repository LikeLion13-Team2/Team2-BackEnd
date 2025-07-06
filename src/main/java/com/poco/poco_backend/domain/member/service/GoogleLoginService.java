package com.poco.poco_backend.domain.member.service;

import com.poco.poco_backend.domain.member.dto.request.GoogleMemberDTO;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.entity.Token;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.domain.member.repository.TokenRepository;
import com.poco.poco_backend.global.code.AuthErrorCode;
import com.poco.poco_backend.global.security.auth.AuthException;
import com.poco.poco_backend.global.security.auth.CustomUserDetails;
import com.poco.poco_backend.global.security.auth.CustomUserDetailsService;
import com.poco.poco_backend.global.security.jwt.JwtDTO;
import com.poco.poco_backend.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleLoginService {

    @Value("${oauth.google.client-id}")
    private String clientId;
    @Value("${oauth.google.google-secret}")
    private String clientSecret;
    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    private final WebClient webClient = WebClient.create();

    public GoogleMemberDTO getMemberInfo(String code) {
        // 1-1 access token 요청
        String tokenRequestBody = "code=" + code +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code";

        Map<String, Object> tokenResponse = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(tokenRequestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        log.info("[WebClient] Token Response: {}", tokenResponse);

        String accessToken = (String) tokenResponse.get("access_token");

        // 1-2 사용자 정보 요청
        Map<String, Object> memberInfo = webClient.get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        log.info("[WebClient] Member Info Response: {}", memberInfo);

        return new GoogleMemberDTO(
                (String) memberInfo.get("email"),
                (String) memberInfo.get("name")
        );
    }

    public Member signupOrGetMember(GoogleMemberDTO googleMemberDto) {
        return memberRepository.findByEmail(googleMemberDto.email())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(googleMemberDto.email())
                            .nickname(googleMemberDto.nickname())
                            .password("")
                            .roles("ROLE_USER")
                            .build();
                    return memberRepository.save(newMember);
                });
    }

    public String createJwt(Member member) {
        CustomUserDetails newUserDetails = new CustomUserDetails(
                member.getEmail(), member.getPassword(), member.getRoles());

        String refreshToken = jwtUtil.createJwtRefreshToken(newUserDetails);
        Token token = Token.builder()
                .email(member.getEmail())
                .token(refreshToken)
                .build();
        tokenRepository.save(token);

        return jwtUtil.createJwtAccessToken(newUserDetails);
    }

    public JwtDTO reissueToken(JwtDTO jwtDto) throws SignatureException {
        log.info("[ Auth Service ] 토큰 재발급을 시작합니다.");
        String accessToken = jwtDto.jwtAccessToken();
        String refreshToken = jwtDto.jwtRefreshToken();

        String email = jwtUtil.getEmail(refreshToken);
        log.info("[ Auth Service ] Email ---> {}", email);

        Token refreshTokenByDB = tokenRepository.findByEmail(email).orElseThrow(
                () -> new AuthException(AuthErrorCode.UNAUTHORIZED_401)
        );

        jwtUtil.validateToken(refreshToken);

        log.info("[ Auth Service ] Refresh Token 이 유효합니다.");

        if (refreshTokenByDB.getToken().equals(refreshToken)) {
            log.info("[ Auth Service ] 토큰을 재발급합니다.");
            return jwtUtil.reissueToken(refreshToken);
        } else {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED_401);
        }
    }
}