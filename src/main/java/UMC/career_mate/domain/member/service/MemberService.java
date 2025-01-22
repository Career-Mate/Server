package UMC.career_mate.domain.member.service;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.Service.JobService;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.converter.MemberConverter;
import UMC.career_mate.domain.member.dto.request.JoinMemberDTO;
import UMC.career_mate.domain.member.dto.response.MemberInfoDTO;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
import UMC.career_mate.domain.planner.service.PlannerCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JobService jobService;
    private final PlannerCommandService plannerService;

    @Transactional
    public Member joinMember(JoinMemberDTO request) {
        Job job = jobService.findJobById(request.job());

        //새로운 회원 객체 생성
        Member newMember = MemberConverter.toEntity(request, job);

        //비어있는 플래너 생성
        CreatePlannerDTO createPlannerDTO = CreatePlannerDTO.builder().build();
        plannerService.savePlanner(newMember,createPlannerDTO);

        return memberRepository.save(newMember);
    }

    public MemberInfoDTO getMemberInfo(Member member) {
        return MemberConverter.toMemberInfo(member);
    }
}
