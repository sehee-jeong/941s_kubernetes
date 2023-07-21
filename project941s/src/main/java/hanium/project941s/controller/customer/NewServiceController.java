package hanium.project941s.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NewServiceController {
    @RequestMapping("/customer/newService")
    public String newService(Model model){
        return "customer/newService";
    }
}
