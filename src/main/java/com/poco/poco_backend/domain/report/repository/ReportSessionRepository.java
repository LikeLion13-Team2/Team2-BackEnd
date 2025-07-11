package com.poco.poco_backend.domain.report.repository;

import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.entity.ReportSession;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportSessionRepository extends JpaRepository<ReportSession, Long> {

    @Modifying
    @Query("DELETE FROM ReportSession s WHERE s.studySession = :studySession")
    int deleteReportStudySessionByStudySession(@Param("studySession")StudySession studySession);
}
