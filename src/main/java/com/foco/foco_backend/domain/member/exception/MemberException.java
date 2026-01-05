package com.foco.foco_backend.domain.member.exception;

import com.foco.foco_backend.global.code.BaseErrorCode;
import com.foco.foco_backend.global.exception.CustomException;

public class MemberException extends CustomException {
    public MemberException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
