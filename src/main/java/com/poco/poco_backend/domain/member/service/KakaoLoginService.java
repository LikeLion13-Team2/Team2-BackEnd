package com.poco.poco_backend.domain.member.service;

import com.poco.poco_backend.domain.member.dto.request.KakaoMemberDTO;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.global.security.jwt.JwtUtil;
import com.poco.poco_backend.global.security.jwt.KakaoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {
    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public Member oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {
        KakaoMemberDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        return null;
    }


}
