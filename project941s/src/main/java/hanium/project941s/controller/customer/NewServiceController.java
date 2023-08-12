package hanium.project941s.controller.customer;

import hanium.project941s.dto.NewServiceDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NewServiceController {
    @GetMapping("/customer/newService")
    public String newService(Model model){
        model.addAttribute("form", new NewServiceDTO());
        return "customer/newService";
    }

    @PostMapping("/customer/deploy")
    public String deployNewService(@ModelAttribute("form") NewServiceDTO formDto,
                                   @RequestParam("keys") String[] keys,
                                   @RequestParam("values") String[] values) {
        for (int i = 0; i < keys.length; i++) {
            formDto.getEnv().put(keys[i], values[i]);
        }
        System.out.println(formDto);
        return "customer/deploySuccess";
    }
}
