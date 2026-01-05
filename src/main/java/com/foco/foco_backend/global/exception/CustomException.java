package com.foco.foco_backend.global.exception;

import com.foco.foco_backend.global.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final BaseErrorCode code;

    @Override
    public String getMessage() {
        return code.getMessage();
    }

}
