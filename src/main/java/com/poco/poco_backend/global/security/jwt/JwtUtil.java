package com.poco.poco_backend.global.security.jwt;

import com.poco.poco_backend.domain.member.entity.Token;
import com.poco.poco_backend.domain.member.repository.TokenRepository;
import com.poco.poco_backend.global.security.auth.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;
    private final TokenRepository tokenRepository;



    public JwtUtil(
            @Value("${JWT_SECRET}") String secret,
            @Value("${JWT_ACCESS_EXPIRATION}") Long access,
            @Value("${JWT_REFRESH_EXPIRATION}") Long refresh,
            TokenRepository tokenRepo
    ) {
        //시크릿 키 생성 + 각종 필드 초기화
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        accessExpMs = access;
        refreshExpMs = refresh;
        tokenRepository = tokenRepo;
    }


    //토큰에서 이메일 추출
    public String getEmail(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseEncryptedClaims(token)
                .getPayload()
                .getSubject();
    }
    //토큰에서 role추출
    public String getRoles(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String tokenProvider(CustomUserDetails customUserDetails, Instant expiration) {

        log.info("[ JwtUtil ] 토큰을 새로 생성합니다.");

        //현재 시간 (토큰 생성 시간)
        Instant issuedAt = Instant.now();

        //토큰에 부여할 권한
        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(customUserDetails.getUsername())
                .claim("role", authorities)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    //accessToken 생성
    public String createJwtAccessToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(accessExpMs);
        return tokenProvider(customUserDetails, expiration);
    }

    //refreshToken 생성, 저장
    public String createJwtRefreshToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(refreshExpMs);
        String refreshToken = tokenProvider(customUserDetails, expiration);


        tokenRepository.save(Token.builder()
                .email(customUserDetails.getUsername())
                .token(refreshToken)
                .build()
        );


        return refreshToken;
    }

    //refreshToken을 활용해서 accessToken 재발급
    public JwtDTO reissueToken(String refreshToken) throws SignatureException {

        CustomUserDetails userDetails = new CustomUserDetails(
                getEmail(refreshToken),
                null,
                getRoles(refreshToken)
        );
        log.info("[ JwtUtil ] 새로운 토큰을 재발급 합니다.");

        //재발급
        return new JwtDTO(
                createJwtAccessToken(userDetails),
                createJwtRefreshToken(userDetails)
        );

    }

    //HTTP 요청의 'Authorization' 헤더에서 JWT 엑세스 토큰을 검색
    //request에서 token을 추출
    public String resolveAccessToken(HttpServletRequest request) {
        log.info("[ JwtUtil ] 헤더에서 토큰을 추출합니다.");
        String tokenFromHeader = request.getHeader("Authorization");

        //헤더에 토큰이 없거나, beartoken이 아닐 때
        if (tokenFromHeader == null || !tokenFromHeader.startsWith("Bearer ")){
            log.warn("[ JwtUtil ] Request Header 에 토큰이 존재하지 않습니다.");
            return null;
        }

        log.info(" [ JwtUtil ] 헤더에 토큰이 존재합니다.");

        return tokenFromHeader.split(" ")[1]; //Bearer 와 분리
    }

    //토큰의 유효성 검사
    public void validateToken(String token) {
        log.info("[ JwtUtil ] 토큰의 유효성을 검증합니다.");
        try {
            long seconds = 3 * 60;
            boolean isExpired = Jwts
                    .parser()
                    .clockSkewSeconds(seconds)
                    .verifyWith(secretKey)
                    .build()
                    .parseEncryptedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
            if (isExpired) {
                log.info("만료된 JWT 토큰입니다.");
            }
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            //원하는 Exception throw
            throw new SecurityException("잘못된 토큰입니다");
        } catch (ExpiredJwtException e) {
            //원하는 Exception throw
            throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
        }
    }


}
