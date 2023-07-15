package hanium.project941s.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homeController {
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("pageName", "소개");
        return "home";
    }
}
