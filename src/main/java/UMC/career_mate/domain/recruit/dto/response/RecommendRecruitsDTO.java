package UMC.career_mate.domain.recruit.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record RecommendRecruitsDTO(
    String jobName,
    List<RecruitThumbNailInfoDTO> recruitThumbNailInfoDTOList
) {

    @Builder
    public record RecruitThumbNailInfoDTO(
        Long recruitId,
        String companyName,
        String title,
        String deadLine,
        boolean isScrapped,

        /**
         * TODO: 아래로는 (필터링, 정렬) 잘 되는지 확인용 데이터, 나중에 삭제 예정
         */
        Integer experienceLevelCode, // 경력 코드 : 0(경력무관), 1(신입), 2(경력), 3(신입/경력)
        Integer experienceLevelMin, // 경력 년수 최소 값
        Integer experienceLevelMax, // 경력 년수 최대 값
        String experienceLevelName,
        Integer educationLevelCode, // 학력 코드 0(학력무관), 1(고등학교졸업), 2(대학졸업(2,3년)), 3(대학졸업(4년)), 4(석사졸업), 5(박사졸업) 등
        String educationLevelName,
        LocalDateTime postingDate
    ) {

    }

}
