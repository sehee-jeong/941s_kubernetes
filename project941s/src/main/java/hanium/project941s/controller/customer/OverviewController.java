package hanium.project941s.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OverviewController {
    @RequestMapping("/customer/overview")
    public String overview(Model model){
        return "customer/overview";
    }
}
