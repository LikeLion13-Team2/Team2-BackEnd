package com.poco.poco_backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {

    @Id
    private String email;

    private String token;

    @OneToOne
    @Setter
    @JoinColumn(name = "member_id") // 외래키
    private Member member;

}
