package com.poco.poco_backend.domain.member.service;


import com.poco.poco_backend.domain.member.dto.response.MemberResponseDTO;
import com.poco.poco_backend.domain.member.entity.Goal;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.repository.GoalRepository;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.domain.member.repository.TokenRepository;
import com.poco.poco_backend.domain.report.repository.ReportRepository;
import com.poco.poco_backend.domain.studySession.repostitory.StudySessionRepository;
import com.poco.poco_backend.global.exception.CustomException;
import com.poco.poco_backend.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SignatureException;
import java.util.List;

import static com.poco.poco_backend.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
public class MemberService {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final StudySessionRepository studySessionRepository;
    private final ReportRepository reportRepository;
    private final GoalRepository goalRepository;

    public MemberService (
            JwtUtil jwtUtil,
            TokenRepository tokenRepository,
            MemberRepository memberRepository,
            StudySessionRepository studySessionRepository,
            ReportRepository reportRepository,
            GoalRepository goalRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
        this.studySessionRepository = studySessionRepository;
        this.reportRepository = reportRepository;
        this.goalRepository = goalRepository;
    }

    //로그아웃 메서드
    @Transactional
    public void memberLogout(String email) throws SignatureException {

        log.info("[ Logout ] 로그아웃을 진행합니다.");
        int deletedTokenCount = tokenRepository.deleteByEmail(email);

        if (deletedTokenCount == 0)
            log.warn("[ Logout ]삭제할 토큰이 존재하지 않습니다.");
        else
            log.info("[ Logout ] 로그아웃이 완료되었습니다.");
    }

    //회원탈퇴 메서드
    @Transactional
    public void deleteMember(String email) throws SignatureException {

        log.info("[ Delete Member ] 회원 탈퇴를 진행합니다.");

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        int deletedSession = studySessionRepository.deleteByMember(member);
        log.info("[ Delete Member ] 회원 학습 세션 {}건 삭제 완료", deletedSession);

        int deletedReport = reportRepository.deleteByMember(member);
        log.info("[ Delete Member ] 회원 학습 리포트 {}건 삭제 완료", deletedReport);

        //멤버를 삭제할 시, refreshToken 또한 함께 제거
        int tokenDeleted = tokenRepository.deleteByEmail(email);
        log.info("[ Delete Member ] 회원 토큰 삭제 완료");

        int memberDeleted = memberRepository.deleteMemberByEmail(email);
        log.info("[ Delete Member ] 회원 삭제 완료");

        if (memberDeleted == 0)
            log.warn("[ Delete Member ] 삭제할 회원이 존재하지 않습니다.");
        else if (tokenDeleted == 0)
            log.warn("[ Delete Member ] 삭제할 토큰이 존재하지 않습니다.");
        else
            log.info("[ Delete Member ] 회원 탈퇴가 완료되었습니다.");

    }

    //회원 조회
    @Transactional(readOnly = true)
    public MemberResponseDTO.MemberWithGoalsResponse getMemberInfo(HttpServletRequest request) throws SignatureException {


        String accessToken = jwtUtil.resolveAccessToken(request);

        String email = jwtUtil.getEmail(accessToken);

        log.info("[ getMemberInfo ] email 을 기반으로 member 를 추출합니다.");
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        log.info("[ getMemberInfo ] member 를 기반으로 goal의 리스트를 생성합니다.");
        List<MemberResponseDTO.GoalDTO> goals = member.getMemberGoals().stream()
                .map(mg -> {
                    Goal g = mg.getGoal();
                    return new MemberResponseDTO.GoalDTO(g.getGoalName());
                })
                .toList();

        //멤버의 이름과 목표(목표의 이름이 담긴 리스트)를 DTO에 담아 반환
        return new MemberResponseDTO.MemberWithGoalsResponse(member.getName(), goals);
    }

    //이름 변경
    @Transactional
    public void changeMemberName(String email, String newName) throws SignatureException {

        int nameChanged = memberRepository.updateMemberByEmail(email, newName);

        if (nameChanged == 0)
            log.warn("[ changeMemberName ] 이름을 변경할 회원이 존재하지 않습니다.");
        else
            log.info("[ changeMemberName ] 회원 이름 {}건 변경 완료", nameChanged);
    }

    @Transactional
    public void setMemberGoal(String email, String goalName) throws SignatureException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        Goal newGoal = goalRepository.findGoalByGoalName(goalName)
                .orElseGet(() -> goalRepository.save(new Goal(goalName)));

        //Goal이 담긴 리스트에서 이름이 같은 것이 있으면 스킵
        boolean alreadyExists = member.getMemberGoals().stream()
                .anyMatch(mg -> mg.getGoal().getGoalName().equals(goalName));
        if (!alreadyExists) {
            member.addGoal(newGoal);
            log.info("[ setMemberGoal ] 새로운 goal: {}을(를) 추가했습니다.", goalName);
        }
    }

    @Transactional
    public void deleteMemberGoal(String email, String goalName) throws SignatureException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        member.deleteGoal(goalName);

    }
}
