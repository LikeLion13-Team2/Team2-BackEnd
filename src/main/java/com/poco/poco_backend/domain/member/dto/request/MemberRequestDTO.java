package com.poco.poco_backend.domain.member.dto.request;

public class MemberRequestDTO {

    public record ChangeNameDTO(
            String newName
    ) {}
}
