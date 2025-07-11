package com.poco.poco_backend.domain.member.entity;


import java.time.LocalDateTime;
import com.poco.poco_backend.common.entity.BaseTimeEntity;
import com.poco.poco_backend.domain.report.entity.Report;
import com.poco.poco_backend.domain.studySession.entity.StudySession;
import com.poco.poco_backend.global.security.auth.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Token token;
  
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudySession> studySessions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGoal> memberGoals = new ArrayList<>();

    //memberGoal을 memberGoals 리스트에 추가
    public void addGoal(Goal goal) {
        MemberGoal memberGoal = new MemberGoal(this, goal);
        memberGoals.add(memberGoal);
    }

    public void resetGoal() {
        memberGoals.clear();
    }

    //goalName을 넣으면 해당 이름을 가진 Goal 객체를 가진 요소를 삭제
    public void deleteGoal(String goalName) {
        memberGoals.removeIf(mg -> mg.getGoal().getGoalName().equals(goalName));
    }

}