package com.foco.foco_backend.domain.member.repository;

import com.foco.foco_backend.domain.member.entity.Member;
import com.foco.foco_backend.domain.member.entity.MemberGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberGoalRepository extends JpaRepository<MemberGoal, Long> {

}
