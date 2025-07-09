package com.poco.poco_backend.domain.report.exception;

import com.poco.poco_backend.global.code.BaseErrorCode;
import com.poco.poco_backend.global.exception.CustomException;

public class ReportException extends CustomException {
    public ReportException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
