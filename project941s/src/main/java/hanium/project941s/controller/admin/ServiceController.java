package hanium.project941s.controller.admin;


import hanium.project941s.domain.Member;
import hanium.project941s.domain.MemberService;
import hanium.project941s.repository.MemberRepository;
import hanium.project941s.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ServiceController {

    private final MemberRepository memberRepository;
    private final ServiceRepository serviceRepository;
    @RequestMapping("/admin/service")
    public String service(@RequestParam Long memberId, Model model) {
        List<MemberService> services = serviceRepository.findByMember_Id(memberId);
        Member member = memberRepository.getOne(memberId);

        model.addAttribute("services", services);
        model.addAttribute("member", member);

        return "admin/service";
    }
}
