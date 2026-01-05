package com.foco.foco_backend.domain.studySession.exception;

import com.foco.foco_backend.global.code.BaseErrorCode;
import com.foco.foco_backend.global.exception.CustomException;

public class StudySessionException extends CustomException {
    public StudySessionException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
