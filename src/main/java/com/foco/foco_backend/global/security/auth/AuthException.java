package com.foco.foco_backend.global.security.auth;

import com.foco.foco_backend.global.code.BaseErrorCode;
import com.foco.foco_backend.global.exception.CustomException;

public class AuthException extends CustomException {

    public AuthException(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
