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
import jakarta.annotation.PostConstruct;
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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleLoginService {

    @Value("${oauth.google.client_id}")
    private String clientId;
    @Value("${oauth.google.google_secret}")
    private String clientSecret;
    @Value("${oauth.google.redirect_uri}")
    private String redirectUri;

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    private RestTemplate restTemplate;

    @PostConstruct
    private void initRestTemplate() {

        log.info("[GoogleLoginService] clientId: {}", clientId);
        log.info("[GoogleLoginService] clientSecret: {}", clientSecret);
        log.info("[GoogleLoginService] redirectUri: {}", redirectUri);

        this.restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.addAll(restTemplate.getMessageConverters());

        this.restTemplate.setMessageConverters(converters);
    }

    //code로 access token 요청 (사용자 정보 반환)
    public GoogleMemberDTO getMemberInfo(String code) {
        //발급받은 인가 코드 디코딩
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);

        //1-1 access token 요청
        //헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", decodedCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");




        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);

        log.info("Google Token Request Body = {}", body);
        log.info("Google Token Headers = {}", headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", tokenRequest, Map.class);

        log.info("Google Token Request Body = {}", body);
        log.info("Google Token Headers = {}", headers);

        log.info("[GoogleLoginService] Token Response: {}", tokenResponse.getBody());

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        //1-2 사용자 정보 요청
        HttpHeaders memberHeaders = new HttpHeaders();
        memberHeaders.setBearerAuth(accessToken);
        HttpEntity<?> memberRequest = new HttpEntity<>(memberHeaders);

        ResponseEntity<Map> memberInfoResponse = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                memberRequest,
                Map.class
        );

        Map<String, Object> memberInfo = memberInfoResponse.getBody();

        log.info("[GoogleLoginService] Member Info Response: {}", memberInfo);

        return new GoogleMemberDTO(
                (String) memberInfo.get("email"),
                (String) memberInfo.get("name")
        );

    }

    //추출한 내용을 바탕으로 db에서 해당 유저를 조회한 후, 없으면 생성
    public Member signupOrGetMember(GoogleMemberDTO googleMemberDto) {
        return memberRepository.findByEmail(googleMemberDto.email())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(googleMemberDto.email())
                            .name(googleMemberDto.name())
                            .password("")
                            .roles("ROLE_USER")
                            .build();
                    return memberRepository.save(newMember);
                });
    }

    //Jwt 생성
    public String createJwt(Member member) {

        CustomUserDetails newUserDetails = new CustomUserDetails(
                member.getEmail(), member.getPassword(), member.getRoles());

        //refresh 토큰 발급
        String refreshToken = jwtUtil.createJwtRefreshToken(newUserDetails);
        //리프레시 토큰을 저장하기 위해 token 엔티티를 만든 뒤, refresh 토큰을 담아 저장
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

        //Access Token 으로부터 사용자 Email 추출
        String email = jwtUtil.getEmail(refreshToken); // **수정부분**
        log.info("[ Auth Service ] Email ---> {}", email);

        //Access Token 에서의 Email 로 부터 DB 에 저장된 Refresh Token 가져오기
        Token refreshTokenByDB = tokenRepository.findByEmail(email).orElseThrow(
                () -> new AuthException(AuthErrorCode.UNAUTHORIZED_401)
        );

        //Refresh Token 이 유효한지 검사
        jwtUtil.validateToken(refreshToken);

        log.info("[ Auth Service ] Refresh Token 이 유효합니다.");

        //만약 DB 에서 찾은 Refresh Token 과 파라미터로 온 Refresh Token 이 일치하면 새로운 토큰 발급
        if (refreshTokenByDB.getToken().equals(refreshToken)) {
            log.info("[ Auth Service ] 토큰을 재발급합니다.");
            return jwtUtil.reissueToken(refreshToken);
        } else {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED_401);
        }

    }
}
