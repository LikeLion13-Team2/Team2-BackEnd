package com.poco.poco_backend.domain.member.exception;

import com.poco.poco_backend.global.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "회원을 찾을 수 없습니다."),
    PASSWORD_UNCHANGED(HttpStatus.CONFLICT, "MEMBER409_1", "새 비밀번호가 기존 비밀번호와 같습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
