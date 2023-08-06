package hanium.project941s.controller.admin;

import hanium.project941s.domain.Member;
import hanium.project941s.domain.MemberService;
import hanium.project941s.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @RequestMapping("/admin/member")
    public String member(Model model, @PageableDefault(size = 1) Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);

        int startPage = 1;
        int endPage = Math.max(1, page.getTotalPages());

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("members", page);

        return "admin/member";
    }
}
