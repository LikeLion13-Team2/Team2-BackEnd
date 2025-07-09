package com.poco.poco_backend.domain.report.exception;

import com.poco.poco_backend.global.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT404_1", "리포트를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
