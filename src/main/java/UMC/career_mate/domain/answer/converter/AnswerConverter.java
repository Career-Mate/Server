package UMC.career_mate.domain.answer.converter;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO;
import UMC.career_mate.domain.answer.dto.response.AnswerInfoListDTO;
import UMC.career_mate.domain.answer.dto.response.AnswerInfoDTO;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.question.Question;

import java.util.List;
import java.util.stream.Collectors;

public class AnswerConverter {

    public static Answer toAnswer(AnswerCreateOrUpdateDTO.AnswerInfo answerInfo, Member member, Question question) {
        return Answer.builder()
                .content(answerInfo.content())
                .member(member)
                .question(question)
                .build();
    }

    public static AnswerInfoDTO toAnswerInfoDTO(Answer answer) {
        return AnswerInfoDTO.builder()
                .questionId(answer.getQuestion().getId())
                .questionName(answer.getQuestion().getContent())
                .content(answer.getContent())
                .build();
    }

    public static List<AnswerInfoDTO> toAnswerInfoDTOList(List<Answer> answerList) {
        return answerList.stream()
                .map(AnswerConverter::toAnswerInfoDTO)
                .collect(Collectors.toList());
    }

    public static AnswerInfoListDTO toAnswerListResponseDTO(Long sequence, List<Answer> answerList) {
        return AnswerInfoListDTO.builder()
                .sequence(sequence)
                .answerList(toAnswerInfoDTOList(answerList))
                .build();
    }
}
