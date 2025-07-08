package com.poco.poco_backend.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus {

    _PARSING_ERROR(HttpStatus.BAD_REQUEST, "KAKAO 응답 파싱 오류입니다."),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorStatus(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
