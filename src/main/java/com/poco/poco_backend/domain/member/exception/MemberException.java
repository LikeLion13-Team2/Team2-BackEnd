package com.poco.poco_backend.domain.member.exception;

import com.poco.poco_backend.global.code.BaseErrorCode;
import com.poco.poco_backend.global.exception.CustomException;

public class MemberException extends CustomException {
    public MemberException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
