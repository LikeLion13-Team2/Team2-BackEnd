package com.poco.poco_backend.domain.member.controller;


import com.poco.poco_backend.domain.member.dto.request.GoalDTO;
import com.poco.poco_backend.domain.member.dto.request.MemberRequestDTO;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.service.GoogleLoginService;
import com.poco.poco_backend.domain.member.service.MemberService;
import com.poco.poco_backend.global.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "Member", description = "멤버 관련 API by 한민")
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
    @DeleteMapping("/me")
    public CustomResponse<?> deleteMember(HttpServletRequest request) throws SignatureException {

        memberService.deleteMember(request);

        return CustomResponse.onSuccess("회원 탈퇴 성공");
    }

    //회원 정보 조회
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "회원 정보 조회", description = """
            회원 정보 조회 api 입니다.\n
            access_token 정보를 바탕으로 회원의 email, goal 을 반환합니다.
            """)
    @GetMapping("/me")
    public CustomResponse<?> getMemberInfo(HttpServletRequest request) throws SignatureException {

        return CustomResponse.onSuccess(memberService.getMemberInfo(request));
    }

    //사용자 이름 변경
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "회원 이름 변경", description = "회원 이름 변경 api 입니다.")
    @PatchMapping("/name")
    public CustomResponse<?> changeName(HttpServletRequest request,
                                        @RequestBody MemberRequestDTO.ChangeNameDTO changeNameDTO)
            throws SignatureException {

        String newName = changeNameDTO.newName();

        memberService.changeMemberName(request, newName);

        return CustomResponse.onSuccess("회원 이름 변경이 완료되었습니다.");
    }

    //목표 추가
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "목표 추가", description = "목표 추가 api 입니다.")
    @PutMapping("/goals")
    public CustomResponse<?> setGoal(HttpServletRequest request,
                                     @RequestBody GoalDTO.UpdateGoalDTO updateGoalDTO)
            throws SignatureException {

        String goalName = updateGoalDTO.goalName();

        memberService.setMemberGoal(request, goalName);

        return CustomResponse.onSuccess("목표 추가가 완료 되었습니다.");
    }

    //목표 삭제
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "목표 삭제", description = "목표 삭제 api 입니다.")
    @DeleteMapping("/goals/{goalName}")
    public CustomResponse<?> deleteGoal(HttpServletRequest request,
                                        @PathVariable String goalName)
            throws SignatureException {


        memberService.deleteMemberGoal(request, goalName);

        return CustomResponse.onSuccess("목표 삭제가 완료 되었습니다.");
    }



}
