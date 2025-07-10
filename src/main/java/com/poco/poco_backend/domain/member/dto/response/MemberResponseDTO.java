package com.poco.poco_backend.domain.member.dto.response;

import java.util.List;

public class MemberResponseDTO {

    public record MemberInfoDTO(
            String name,
            List<GoalDTO> goals
    ) {}

    public record GoalDTO(
            String goalName
    ) {}

    public record MemberWithGoalsResponse(
            String memberName,
            List<GoalDTO> goals
    ) {}
}
