package com.poco.poco_backend.domain.studySession.repostitory;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findAllByMemberEmail(String email);

    List<StudySession> findByMemberAndStartedAtBetween(Member member, LocalDateTime start, LocalDateTime end);
}
