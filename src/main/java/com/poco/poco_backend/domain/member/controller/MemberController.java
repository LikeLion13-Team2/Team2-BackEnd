package com.poco.poco_backend.domain.member.controller;


import com.poco.poco_backend.domain.member.service.GoogleLoginService;
import com.poco.poco_backend.domain.member.service.MemberService;
import com.poco.poco_backend.global.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Member", description = "멤버 관련 API by 한민.")
public class MemberController {

    private final GoogleLoginService googleLoginService;
    private final MemberService memberService;


    //로그아웃 api
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "로그아웃", description = "로그아웃 api 입니다.")
    @DeleteMapping("/logout")
    public CustomResponse<?> logout(HttpServletRequest request) throws SignatureException {

        memberService.memberLogout(request);

        return CustomResponse.onSuccess("로그아웃 성공");
    }

    //회원 탈퇴 api
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "회원 탈퇴", description = """
            회원 탈퇴 api 입니다. \n
            로그아웃 성공 시, 클라이언트 측 accessToken 및 회원 데이터도 삭제해주시면 감사하겠습니다.
            """)
    @DeleteMapping("/members/me")
    public CustomResponse<?> deleteMember(HttpServletRequest request) throws SignatureException {

        memberService.deleteMember(request);

        return CustomResponse.onSuccess("회원 탈퇴 성공");
    }

}
