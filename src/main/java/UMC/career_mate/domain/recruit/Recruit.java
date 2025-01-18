package UMC.career_mate.domain.recruit;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채용 공고 목록 조회 시 아래 4개만 반환
    private String companyName; // 회사명
    private String title; // 공고 제목
    private String imageUrl; // 공고 이미지
    private LocalDateTime deadLine; // 마감일

    // 요약 페이지 조회 시 아래 데이터 추가 반환
    private String companyInfoUrl; // 회사 정보 url
    private String recruitUrl; // 채용 공고 url

    private Integer experienceLevelCode; // 경력 코드 : 0(경력무관), 1(신입), 2(경력), 3(신입/경력)
    private Integer experienceLevelMin; // 경력 년수 최소 값
    private Integer experienceLevelMax; // 경력 년수 최대 값
    private String experienceLevelName; // 경력 값 ex) 경력3년↑

    private Integer educationLevelCode; // 학력 코드 0(학력무관), 1(고등학교졸업), 2(대학졸업(2,3년)), 3(대학졸업(4년)), 4(석사졸업), 5(박사졸업) 등
    private String educationLevelName; // 학력 값 ex) 대학졸업(2,3년)이상

    private String employmentName; // 고용(근무) 형태 값
    private String salaryName; // 연봉 값

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> jobNames; // 직무 키워드

    private String region; // 근무 지역
    private String industryName; // 산업군

    private LocalDateTime postingDate; // 채용 공고 글 작성일
    private LocalDateTime openingDate; // 채용 공고 원서 접수 시작일
}
