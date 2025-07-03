package com.poco.poco_backend.global.security.auth;

import com.poco.poco_backend.global.code.BaseErrorCode;
import com.poco.poco_backend.global.exception.CustomException;

public class AuthException extends CustomException {

    public AuthException(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
