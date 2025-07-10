package com.poco.poco_backend.domain.report.repository;

import com.poco.poco_backend.common.enums.PeriodType;
import com.poco.poco_backend.domain.member.entity.Member;
import com.poco.poco_backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByMemberAndPeriodTypeAndBaseDate(
            Member member, PeriodType periodType, LocalDate baseDate);

    //멤버 기반 학습 리포트 삭제
    @Modifying
    @Query("DELETE FROM Report r WHERE r.member = :member")
    int deleteByMember(@Param("member") Member member);
}
