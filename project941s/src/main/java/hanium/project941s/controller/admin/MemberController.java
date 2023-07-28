package hanium.project941s.controller.admin;

import hanium.project941s.domain.Member;
import hanium.project941s.domain.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MemberController {
    @RequestMapping("/admin/member")
    public String user(Model model){
        Member member1 = Member
                .builder()
                .name("박찬규")
                .date(new Date())
                .latestDate(new Date())
                .build();

        Member member2 = Member
                .builder()
                .name("찬규 박")
                .date(new Date())
                .latestDate(new Date())
                .build();

        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        model.addAttribute("members", members);

        return "admin/member";
    }
}
