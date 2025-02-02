package UMC.career_mate.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponseDTO<T>(

        int page,
        Boolean hasNext,
        Integer totalPages,
        T result

) {

}
