package com.poco.poco_backend.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AuthErrorCode implements BaseErrorCode{

    FORBIDDEN_403(HttpStatus.FORBIDDEN, "COMMON403", "접근이 금지되었습니다"),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다"),
    NOT_FOUND_404(HttpStatus.NOT_FOUND, "COMMON404", "요청한 자원을 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    //BaseErrorCode에서 HttpStatus get(Http)Status와 같이 선언 되어 있어서
    //그냥 getStatus를 오버라이드 했습니다.
    @Override
    public HttpStatus getStatus() {
        return null;
    }
}
