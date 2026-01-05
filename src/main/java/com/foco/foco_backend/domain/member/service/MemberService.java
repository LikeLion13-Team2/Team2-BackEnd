package com.foco.foco_backend.domain.member.service;


import com.foco.foco_backend.domain.member.dto.response.MemberResponseDTO;
import com.foco.foco_backend.domain.member.entity.Goal;
import com.foco.foco_backend.domain.member.entity.Member;
import com.foco.foco_backend.domain.member.repository.GoalRepository;
import com.foco.foco_backend.domain.member.repository.MemberGoalRepository;
import com.foco.foco_backend.domain.member.repository.MemberRepository;
import com.foco.foco_backend.domain.member.repository.TokenRepository;
import com.foco.foco_backend.domain.report.repository.ReportRepository;
import com.foco.foco_backend.domain.report.repository.ReportSessionRepository;
import com.foco.foco_backend.domain.studySession.repostitory.StudySessionRepository;
import com.foco.foco_backend.global.exception.CustomException;
import com.foco.foco_backend.global.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SignatureException;
import java.util.List;

import static com.foco.foco_backend.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
public class MemberService {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final StudySessionRepository studySessionRepository;
    private final ReportRepository reportRepository;
    private final GoalRepository goalRepository;
    private final MemberGoalRepository memberGoalRepository;
    private final ReportSessionRepository reportSessionRepository;

    public MemberService (
            JwtUtil jwtUtil,
            TokenRepository tokenRepository,
            MemberRepository memberRepository,
            StudySessionRepository studySessionRepository,
            ReportRepository reportRepository,
            GoalRepository goalRepository,
            MemberGoalRepository memberGoalRepository,
            ReportSessionRepository reportSessionRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
        this.studySessionRepository = studySessionRepository;
        this.reportRepository = reportRepository;
        this.goalRepository = goalRepository;
        this.memberGoalRepository = memberGoalRepository;
        this.reportSessionRepository = reportSessionRepository;
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

        memberRepository.delete(member);
        log.info("[ Delete Member ] 회원 삭제 완료");
    }

    //회원 조회
    @Transactional(readOnly = true)
    public MemberResponseDTO.MemberWithGoalsResponse getMemberInfo(String email) throws SignatureException {


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

        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        member -> member.setName(newName),
                        () -> { throw new CustomException(MEMBER_NOT_FOUND); }
                );
    }
    // 목표 설정
    @Transactional
    public void setMemberGoal(String email, String goalName) throws SignatureException {

        //멤버 가져오기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        //목표 필드가 없으면 만들고 가져오기
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

    // 사용자 목표 삭제
    @Transactional
    public void deleteMemberGoal(String email, String goalName) throws SignatureException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        member.deleteGoal(goalName);

    }
}
