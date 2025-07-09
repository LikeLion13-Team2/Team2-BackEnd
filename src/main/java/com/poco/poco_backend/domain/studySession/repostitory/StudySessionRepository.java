package com.poco.poco_backend.domain.studySession.repostitory;

import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findAllByMemberEmail(String email);

    List<StudySession> findByMemberAndPeriodTypeAndStartedAtBetween(
            Member member,
            PeriodType periodType,
            LocalDateTime start,
            LocalDateTime end
    );

}
