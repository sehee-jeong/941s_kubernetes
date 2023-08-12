package hanium.project941s.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeployController {

    @PostMapping("/customer/deploySuccess")
    public String createDeploy(Model model){
        return "customer/deploySuccess";
    }
    @GetMapping("/customer/deployFail")
    public String createfailDeploy(Model model){
        return "customer/deployFail";
    }
}
