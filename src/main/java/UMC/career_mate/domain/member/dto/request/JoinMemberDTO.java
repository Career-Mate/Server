package UMC.career_mate.domain.member.dto.request;

import lombok.Builder;

@Builder
public record JoinMemberDTO(
    String name,
    String email,
    String educationLevel,
    String educationStatus,
    Long job,
    String socialType,
    String clientId
) {
}
