package UMC.career_mate.domain.recruit.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SaraminResponseDTO(
    Jobs jobs
) {

    public record Jobs(
        int count,
        int start,
        int total,
        List<Job> job
    ) {
    }

    public record Job(
        String url,
        int active,
        Company company,
        Position position,
        String keyword,
        Salary salary,
        String id,
        @JsonProperty("posting-timestamp")
        long postingTimestamp,
        @JsonProperty("modification-timestamp")
        long modificationTimestamp,
        @JsonProperty("opening-timestamp")
        long openingTimestamp,
        @JsonProperty("expiration-timestamp")
        long expirationTimestamp,
        @JsonProperty("close-type")
        CloseType closeType
    ) {
    }

    public record Company(
        Detail detail
    ) {
        public record Detail(
            String href,
            String name
        ) {
        }
    }

    public record Position(
        String title,
        Industry industry,
        Location location,
        @JsonProperty("job-type") JobType jobType,
        @JsonProperty("job-mid-code") JobMidCode jobMidCode,
        @JsonProperty("job-code") JobCode jobCode,
        @JsonProperty("experience-level") ExperienceLevel experienceLevel,
        @JsonProperty("required-education-level") RequiredEducationLevel requiredEducationLevel
    ) {
    }

    public record Industry(
        String code,
        String name
    ) {
    }

    public record Location(
        String code,
        String name
    ) {
    }

    public record JobType(
        String code,
        String name
    ) {
    }

    public record JobMidCode(
        String code,
        String name
    ) {
    }

    public record JobCode(
        String code,
        String name
    ) {
    }

    public record ExperienceLevel(
        int code,
        int min,
        int max,
        String name
    ) {
    }

    public record RequiredEducationLevel(
        String code,
        String name
    ) {
    }

    public record Salary(
        String code,
        String name
    ) {
    }

    public record CloseType(
        String code,
        String name
    ) {
    }
}
