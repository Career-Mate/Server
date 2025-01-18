package UMC.career_mate.domain.member.service;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.Service.JobService;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.converter.MemberConverter;
import UMC.career_mate.domain.member.dto.request.JoinMemberDTO;
import UMC.career_mate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JobService jobService;

    @Transactional
    public Member joinMember(JoinMemberDTO request) {
        Job job = jobService.findJobById(request.job());
        Member newMember = MemberConverter.toEntity(request, job);

        return memberRepository.save(newMember);
    }
}
