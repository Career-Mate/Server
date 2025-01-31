package UMC.career_mate.domain.chatgpt.converter;

import UMC.career_mate.domain.chatgpt.GptAnswer;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;

public class GptAnswerConverter {

    public static GptAnswer toEntity(Member member, RecruitKeyword recruitKeyword, int careerYear) {
        return GptAnswer.builder()
            .member(member)
            .recruitKeyword(recruitKeyword)
            .careerYear(careerYear)
            .build();
    }
}
