package com.poco.poco_backend.global.security.jwt;

public record JwtDTO(
        String jwtAccessToken,
        String jwtRefreshToken
) {
}
