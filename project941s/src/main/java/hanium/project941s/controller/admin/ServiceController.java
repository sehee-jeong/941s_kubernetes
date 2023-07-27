package hanium.project941s.controller.admin;

import hanium.project941s.domain.Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServiceController {
    @RequestMapping("/admin/service")
    public String service(Model model) {
        Service[] services = new Service[5];


        return "admin/service";
    }
}
