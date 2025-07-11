package com.poco.poco_backend.domain.member.dto.request;

public class GoalDTO {

    public record UpdateGoalDTO(
            String goalNames
    ) {}

    public record UpdateProfileDTO(
            String newName,
            String goalNames
    ) {}


}
