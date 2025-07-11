package com.poco.poco_backend.domain.studySession.repostitory;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findAllByMemberEmail(String email);

    List<StudySession> findByMemberAndStartedAtBetween(Member member, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("DELETE FROM StudySession s WHERE s.member = :member")
    int deleteByMember(@Param("member") Member member);

    @Query("SELECT s FROM StudySession s WHERE s.member = :member")
    List<StudySession> findByMember(@Param("member") Member member);



}

