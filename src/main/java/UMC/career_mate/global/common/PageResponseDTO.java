package UMC.career_mate.global.common;

import lombok.Builder;

@Builder
public record PageResponseDTO<T>(

        int page,
        boolean hasNext,
        T result

) {

}
