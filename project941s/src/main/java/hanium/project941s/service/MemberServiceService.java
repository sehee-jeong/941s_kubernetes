package hanium.project941s.service;

import hanium.project941s.domain.Enums.ActType;
import hanium.project941s.domain.Member;
import hanium.project941s.domain.MemberAct;
import hanium.project941s.domain.MemberService;
import hanium.project941s.dto.NewServiceDTO;
import hanium.project941s.repository.MemberRepository;
import hanium.project941s.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceService {
    private final MemberRepository memberRepository;
    private final ServiceRepository serviceRepository;

    String EksUrl = "http://15.165.46.238:";

    @Transactional
    public boolean createMemberService(NewServiceDTO newServiceDTO, String memberProviderId){
        try{
            Member member = memberRepository.findMemberByMemberProviderId(memberProviderId);
            MemberAct memberAct = MemberAct.builder()
                    .version("1")
                    .sampleDate(new Date())
                    .actType(ActType.CREATE)
                    .build();

            MemberService memberService = MemberService.createMemberService(EksUrl, newServiceDTO, member, memberAct);

            serviceRepository.save(memberService);
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
        return true;
    }

    public int getUniquePort(){
        List<MemberService> memberServices = serviceRepository.findAllByOrderByPortAsc();
        int result = 32000;

        if (memberServices.size() == 0){
            return result;
        }

        for (int i = 0; i < memberServices.size(); i++){
            int tem_result = result + i;

            if (memberServices.get(i).getPort() != tem_result) {
                return tem_result;
            }
        }
        result += memberServices.size();
        return result;
    }
}
