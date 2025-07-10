package com.poco.poco_backend.domain.member.service;


import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.domain.member.repository.TokenRepository;
import com.poco.poco_backend.domain.report.repository.ReportRepository;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import com.poco.poco_backend.domain.studySession.repostitory.StudySessionRepository;
import com.poco.poco_backend.global.exception.CustomException;
import com.poco.poco_backend.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SignatureException;

import static com.poco.poco_backend.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
public class MemberService {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final StudySessionRepository studySessionRepository;
    private final ReportRepository reportRepository;

    public MemberService (
            JwtUtil jwtUtil,
            TokenRepository tokenRepository,
            MemberRepository memberRepository,
            StudySessionRepository studySessionRepository,
            ReportRepository reportRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
        this.studySessionRepository = studySessionRepository;
        this.reportRepository = reportRepository;
    }

    //로그아웃 메서드
    @Transactional
    public void memberLogout(HttpServletRequest request) throws SignatureException {

        String accessToken = jwtUtil.resolveAccessToken(request);

        log.info("[ Logout ] 토큰으로부터 이메일을 추출합니다.");
        String email = jwtUtil.getEmail(accessToken);

        log.info("[ Logout ] 로그아웃을 진행합니다.");
        int deletedTokenCount = tokenRepository.deleteByEmail(email);

        if (deletedTokenCount == 0)
            log.warn("삭제할 토큰이 존재하지 않습니다.");
        else
            log.info("[ Logout ] 로그아웃이 완료되었습니다.");
    }

    //회원탈퇴 메서드
    @Transactional
    public void deleteMember(HttpServletRequest request) throws SignatureException {

        String accessToken = jwtUtil.resolveAccessToken(request);

        log.info("[ Delete Member ] 토큰으로부터 이메일을 추출합니다.");
        String email = jwtUtil.getEmail(accessToken);

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
}
