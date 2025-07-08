package com.poco.poco_backend.domain.studySession.exception;

import com.poco.poco_backend.global.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StudySessionErrorCode implements BaseErrorCode {
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "SESSION401_1", "세션에 접근할 수 있는 권한이 없습니다."),
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404_1", "세션을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
