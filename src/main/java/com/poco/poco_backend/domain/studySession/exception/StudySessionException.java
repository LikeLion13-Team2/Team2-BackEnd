package com.poco.poco_backend.domain.studySession.exception;

import com.poco.poco_backend.global.code.BaseErrorCode;
import com.poco.poco_backend.global.exception.CustomException;

public class StudySessionException extends CustomException {
    public StudySessionException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
