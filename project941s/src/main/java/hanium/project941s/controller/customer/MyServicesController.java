package hanium.project941s.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyServicesController {
    @RequestMapping("/customer/myServices")
    public String MyServices(Model model){
        return "customer/myServices";
    }
}
