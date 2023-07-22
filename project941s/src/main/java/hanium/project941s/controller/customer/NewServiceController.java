package hanium.project941s.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewServiceController {
    @GetMapping("/customer/newService")
    public String newService(Model model){
        return "customer/newService";
    }
}
