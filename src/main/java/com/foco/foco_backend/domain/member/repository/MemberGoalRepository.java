package com.foco.foco_backend.domain.member.repository;

import com.foco.foco_backend.domain.member.entity.MemberGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGoalRepository extends JpaRepository<MemberGoal, Long> {

}
