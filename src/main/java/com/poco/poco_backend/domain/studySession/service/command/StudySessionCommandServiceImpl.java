package com.poco.poco_backend.domain.studySession.service.command;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.member.exception.MemberErrorCode;
import com.poco.poco_backend.domain.member.exception.MemberException;
import com.poco.poco_backend.domain.member.repository.MemberRepository;
import com.poco.poco_backend.domain.studySession.converter.StudySessionConverter;
import com.poco.poco_backend.domain.studySession.dto.request.StudySessionRequestDTO;
import com.poco.poco_backend.domain.studySession.dto.response.StudySessionResponseDTO;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import com.poco.poco_backend.domain.studySession.repostitory.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudySessionCommandServiceImpl implements StudySessionCommandService {

    private final StudySessionRepository studySessionRepository;
    private final MemberRepository memberRepository;

    @Override
    public StudySessionResponseDTO.CreateStudySessionResponseDTO createSession(StudySessionRequestDTO.CreateStudySessionRequestDTO createDTO, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 총 세션 시간
        long sessionSeconds = Duration.between(createDTO.startedAt(), createDTO.endedAt()).toSeconds();

        // 총 딴짓 시간
        long distractionSeconds = createDTO.distractionLogs().stream()
                .mapToLong(log -> Duration.between(log.start(), log.end()).toSeconds())
                .sum();

        // 총 쉬는 시간
        long breakSeconds = createDTO.breakLogs().stream()
                .mapToLong(log -> Duration.between(log.start(), log.end()).toSeconds())
                .sum();

        // 총 집중 시간
        long focusSeconds = sessionSeconds - distractionSeconds - breakSeconds;

        // 최장 집중 지속 시간
        long longestFocusSeconds = calculateLongestFocusSeconds(createDTO);

        // 계산 DTO 생성
        StudySessionRequestDTO.StudySessionCalculations calculations
                = StudySessionRequestDTO.StudySessionCalculations.builder()
                .sessionSeconds(sessionSeconds)
                .focusSeconds(focusSeconds)
                .distractionSeconds(distractionSeconds)
                .breakSeconds(breakSeconds)
                .longestFocusSeconds(longestFocusSeconds)
                .build();

        // 엔티티 생성 및 저장
        StudySession session = StudySessionConverter.toStudySession(createDTO, calculations, member);
        studySessionRepository.save(session);

        return StudySessionConverter.toStudySessionResponseDTO(session);


    }

    // 최장 집중 지속 시간 계산 함수
    private long calculateLongestFocusSeconds(StudySessionRequestDTO.CreateStudySessionRequestDTO dto) {
        LocalDateTime startedAt = dto.startedAt();
        LocalDateTime endedAt = dto.endedAt();
        List<StudySessionRequestDTO.CreateStudySessionRequestDTO.DistractionLog> distractions = dto.distractionLogs();
        List<StudySessionRequestDTO.CreateStudySessionRequestDTO.BreakLog> breaks = dto.breakLogs();

        record TimeRange(LocalDateTime start, LocalDateTime end) {}

        // 집중이 아닌 구간 (딴짓 시간 + 쉬는 시간)
        List<TimeRange> nonFocused = new ArrayList<>();
        distractions.forEach(log -> nonFocused.add(new TimeRange(log.start(), log.end())));
        breaks.forEach(log -> nonFocused.add(new TimeRange(log.start(), log.end())));

        // 시간 순 정렬
        nonFocused.sort(Comparator.comparing(TimeRange::start));

        // 집중 구간 추출 (전체 - nonFocused)
        List<TimeRange> focused = new ArrayList<>();
        LocalDateTime current = startedAt; // current로 기준점 설

        for (TimeRange nonFocusedTime : nonFocused) {
            if (current.isBefore(nonFocusedTime.start())) {
                focused.add(new TimeRange(current, nonFocusedTime.start()));
            }
            current = nonFocusedTime.end();
        }

        if (current.isBefore(endedAt)) {
            focused.add(new TimeRange(current, endedAt));
        }

        // 최장 집중 시간 반환
        return focused.stream()
                .mapToLong(range -> Duration.between(range.start(), range.end()).toSeconds())
                .max()
                .orElse(0L);
    }
}
