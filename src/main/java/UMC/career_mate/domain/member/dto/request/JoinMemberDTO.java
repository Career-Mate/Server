package UMC.career_mate.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record JoinMemberDTO(
        @NotNull String name,
        @NotNull String email,
        @NotNull String educationLevel,
        @NotNull String educationStatus,
        @NotNull Long job,
        @NotNull String socialType,
        @NotNull String clientId
) {
}
