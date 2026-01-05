package com.foco.foco_backend.domain.member.dto.response;

import java.util.List;

public class MemberResponseDTO {

    public record GoalDTO(
            String goalName
    ) {}

    public record MemberWithGoalsResponse(
            String memberName,
            List<GoalDTO> goals
    ) {}
}
