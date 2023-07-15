package hanium.project941s.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServiceController {
    @RequestMapping("/admin/service")
    public String service(Model model) {
        return "admin/service";
    }
}
