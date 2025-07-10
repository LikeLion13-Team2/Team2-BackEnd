package com.poco.poco_backend.domain.member.entity;


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
public class Goal {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String goalName;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGoal> memberGoals = new ArrayList<>();

    public Goal(String goalName) {
        this.goalName = goalName;
    }
}
