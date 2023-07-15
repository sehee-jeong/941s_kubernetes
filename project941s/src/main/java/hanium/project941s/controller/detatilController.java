package hanium.project941s.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class detatilController {
    @RequestMapping("/detail")
    public String detail(Model model) {
        String service_name = "941_s";
        String[] services = new String[]{"콘솔1", "콘솔2", "콘솔3"};
        model.addAttribute("name", "941s");
        model.addAttribute("callCount", 2048);
        model.addAttribute("version", "v0.1.8");
        model.addAttribute("url", "localhost:8080");
        model.addAttribute("services", services);
        return "detail";
    }
}
