package UMC.career_mate.domain.chatgpt.service;

import UMC.career_mate.domain.chatgpt.GptAnswer;
import UMC.career_mate.domain.chatgpt.converter.GptAnswerConverter;
import UMC.career_mate.domain.chatgpt.repository.GptAnswerRepository;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class GptAnswerCommandService {

    private final GptAnswerRepository gptAnswerRepository;

    public void saveGptAnswer(Member member, RecruitKeyword recruitKeyword, int careerYear) {
        GptAnswer gptAnswer = GptAnswerConverter.toEntity(member, recruitKeyword, careerYear);
        gptAnswerRepository.save(gptAnswer);
    }

    public void updateGptAnswer(GptAnswer gptAnswer, RecruitKeyword recruitKeyword, int careerYear) {
        gptAnswer.updateData(recruitKeyword, careerYear);
        gptAnswerRepository.save(gptAnswer);
    }
}
