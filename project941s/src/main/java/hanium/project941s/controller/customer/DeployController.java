package hanium.project941s.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeployController {

    @PostMapping("/customer/deploy")
    public String createDeploy(Model model){
        return "customer/deploySuccess";
    }
    @GetMapping("/customer/deploy")
    public String createfailDeploy(Model model){
        return "customer/deployFail";
    }
}
