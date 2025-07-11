package com.poco.poco_backend.domain.member.controller;


import com.poco.poco_backend.domain.member.dto.request.GoalDTO;
import com.poco.poco_backend.domain.member.dto.request.MemberRequestDTO;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.domain.member.service.GoogleLoginService;
import com.poco.poco_backend.domain.member.service.MemberService;
import com.poco.poco_backend.global.CustomResponse;
import com.poco.poco_backend.global.exception.CustomException;
import com.poco.poco_backend.global.security.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

import static com.poco.poco_backend.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "Member", description = "멤버 관련 API by 한민")
public class MemberController {

    private final GoogleLoginService googleLoginService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    //로그아웃 api
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "로그아웃", description = "로그아웃 api 입니다.")
    @DeleteMapping("/logout")
    public CustomResponse<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails)
            throws SignatureException {

        memberService.memberLogout(userDetails.getUsername());

        return CustomResponse.onSuccess("로그아웃 성공");
    }

    //회원 탈퇴 api
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "회원 탈퇴", description = """
            회원 탈퇴 api 입니다. \n
            로그아웃 성공 시, 클라이언트 측 accessToken 및 회원 데이터도 삭제해주시면 감사하겠습니다.
            """)
    @DeleteMapping("/me")
    public CustomResponse<?> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails)
            throws SignatureException {

        memberService.deleteMember(userDetails.getUsername());

        return CustomResponse.onSuccess("회원 탈퇴 성공");
    }

    //회원 정보 조회
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "회원 정보 조회", description = """
            회원 정보 조회 api 입니다.\n
            access_token 정보를 바탕으로 회원의 email, goal 을 반환합니다.
            """)
    @GetMapping("/me")
    public CustomResponse<?> getMemberInfo(HttpServletRequest request)
            throws SignatureException {

        return CustomResponse.onSuccess(memberService.getMemberInfo(request));
    }


    //프로필 변경 api
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "회원 프로필 변경", description = "회원가입 페이지에서 사용하는 회원 프로필 변경 api 입니다.")
    @PatchMapping("/profile")
    public CustomResponse<?> changeNameGoal(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody GoalDTO.UpdateProfileDTO updateProfileDTO)
            throws SignatureException {

        int goalCount = 0;

        String newName = updateProfileDTO.newName();
        String goalNames = updateProfileDTO.goalNames();
        String email = userDetails.getUsername();

        //문자열을 리스트 형태로 파싱
        String[] goalList = goalNames.split(",");

        memberService.changeMemberName(email, newName);

        //멤버 가져오기
        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        //멤버의 목표 초기화
        member.resetGoal();

        //파싱한 리스트 마다 멤버의 목표에 추가
        for (String goal : goalList) {
            memberService.setMemberGoal(userDetails.getUsername(), goal);
            goalCount++;
        }

        log.info("[ changeNameGoal ] 목표 {}건을 추가했습니다.", goalCount);

        return CustomResponse.onSuccess("프로필 변경이 완료되었습니다.");
    }

    //사용자 이름 변경
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "회원 이름 변경", description = "회원 이름 변경 api 입니다.")
    @PatchMapping("/name")
    public CustomResponse<?> changeName(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody MemberRequestDTO.ChangeNameDTO changeNameDTO)
            throws SignatureException {

        String newName = changeNameDTO.newName();

        String email = userDetails.getUsername();

        memberService.changeMemberName(email, newName);

        return CustomResponse.onSuccess("회원 이름 변경이 완료되었습니다.");
    }

    //목표 추가
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "목표 추가", description = "목표 추가 api 입니다.")
    @PutMapping("/goals")
    public CustomResponse<?> setGoal(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestBody GoalDTO.UpdateGoalDTO updateGoalDTO)
            throws SignatureException {

        //멤버 가져오기
        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        //멤버의 목표 초기화
        member.resetGoal();

        int goalCount = 0;

        //문자열을 리스트 형태로 파싱
        String[] goalList = updateGoalDTO.goalNames().split(",");

        //파싱한 리스트 마다 멤버의 목표에 추가
        for (String goal : goalList) {
            memberService.setMemberGoal(userDetails.getUsername(), goal);
            goalCount++;
        }

        log.info("[ setGoal ] 목표 {}건을 추가했습니다.", goalCount);

        return CustomResponse.onSuccess("목표 추가가 완료 되었습니다.");
    }

    //목표 삭제
    @SecurityRequirement(name = "JWT TOKEN")
    @Operation(summary = "목표 삭제", description = "목표 삭제 api 입니다.")
    @DeleteMapping("/goals/{goalName}")
    public CustomResponse<?> deleteGoal(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable String goalName)
            throws SignatureException {

        memberService.deleteMemberGoal(userDetails.getUsername(), goalName);

        return CustomResponse.onSuccess("목표 삭제가 완료 되었습니다.");
    }
}
