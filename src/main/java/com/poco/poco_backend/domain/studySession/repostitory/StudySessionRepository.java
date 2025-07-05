package com.poco.poco_backend.domain.studySession.repostitory;

import com.poco.poco_backend.domain.studySession.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
}
