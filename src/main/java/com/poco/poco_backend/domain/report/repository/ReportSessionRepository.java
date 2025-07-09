package com.poco.poco_backend.domain.report.repository;

import com.poco.poco_backend.domain.report.entity.ReportSession;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportSessionRepository extends JpaRepository<ReportSession, Long> {

    @Query("SELECT rs.studySession FROM ReportSession rs WHERE rs.report.id = :reportId")
    List<StudySession> findAllStudySessionsByReportId(@Param("reportId") Long reportId);


}
