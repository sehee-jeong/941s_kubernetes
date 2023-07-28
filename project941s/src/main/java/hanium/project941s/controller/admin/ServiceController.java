package hanium.project941s.controller.admin;


import hanium.project941s.domain.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ServiceController {
    @RequestMapping("/admin/service")
    public String service(Model model) {
        MemberService service1 = MemberService
                .builder()
                .name("941s")
                .date(new Date())
                .version("1.2.1")
                .githubUrl("https//github.com1")
                .build();

        MemberService service2 = MemberService
                .builder()
                .name("구사일생")
                .date(new Date())
                .version("1.0.2")
                .githubUrl("https//github.com2")
                .build();

        List<MemberService> services = new ArrayList<>();
        services.add(service1);
        services.add(service2);

        model.addAttribute("services", services);

        return "admin/service";
    }
}
