package UMC.career_mate.domain.answer.dto.request;

import UMC.career_mate.domain.answer.dto.response.AnswerResponse;

import java.util.List;

public record AnswerRequest(List<AnswerResponse> answerList) {}

