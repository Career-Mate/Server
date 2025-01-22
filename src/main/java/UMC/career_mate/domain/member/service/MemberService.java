package UMC.career_mate.domain.member.service;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.Service.JobService;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.converter.MemberConverter;
import UMC.career_mate.domain.member.dto.request.CreateProfileDTO;
import UMC.career_mate.domain.member.enums.SocialType;
import UMC.career_mate.domain.member.dto.request.JoinMemberDTO;
import UMC.career_mate.domain.member.dto.response.MemberInfoDTO;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
import UMC.career_mate.domain.planner.service.PlannerCommandService;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JobService jobService;
    private final PlannerCommandService plannerService;

    @Transactional
    public Member makeProfile(CreateProfileDTO request,Member member) {
        Job job = jobService.findJobById(request.job());

        Member profileMember = findMemberByMemberId(member.getId());
        //회원 프로필 작성
        profileMember.createProfile(request, job);
        profileMember.completeProfile();

        //비어있는 플래너 생성
        CreatePlannerDTO createPlannerDTO = CreatePlannerDTO.builder().specifics("specifics").build();
        plannerService.savePlanner(profileMember,createPlannerDTO);

        return profileMember;
    }

    public Boolean checkExistMember(String clientId,SocialType socialType) {
        return memberRepository.existsMemberByClientIdAndSocialType(clientId, socialType);
    }

    public Member findMemberByClientIdAndSocialType(String clientId,SocialType socialType) {
        return memberRepository.findMemberByClientIdAndSocialType(clientId,socialType).orElseThrow(
                () -> new GeneralException(CommonErrorCode.NOT_FOUND_BY_CLIENT_ID)
        );
    }

    @Transactional
    public Member saveEmptyMember(String clientId, SocialType socialType) {
        return memberRepository.save(
                MemberConverter.toEmptyEntity(clientId, socialType)
        );
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new GeneralException(CommonErrorCode.NOT_FOUND_BY_MEMBER_ID)
        );
    }

    public MemberInfoDTO getMemberInfo(Member member) {
        return MemberConverter.toMemberInfo(member);
    }
}

    public Member findMemberByIdAndClientId(Long memberId, String clientId) {
        return memberRepository.findMemberByIdAndClientId(memberId, clientId).orElseThrow(
                () -> new GeneralException(CommonErrorCode.NOT_FOUND_BY_ID_AND_CLIENT_ID)
        );
    }

}
//{
//        "name": "temp",
//        "email": "temp",
//        "educationLevel": "HIGH",
//        "educationStatus": "GRADUATED",
//        "job": 1,
//        "socialType": "NAVER",
//        "clientId": "string"
//        }