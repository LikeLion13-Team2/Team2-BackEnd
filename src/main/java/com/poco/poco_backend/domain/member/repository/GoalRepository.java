package com.poco.poco_backend.domain.member.repository;

import com.poco.poco_backend.domain.member.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    @Query("SELECT g FROM Goal g WHERE g.goalName = :goalName")
    Optional<Goal> findGoalByGoalName(@Param("goalName") String goalName);


}
