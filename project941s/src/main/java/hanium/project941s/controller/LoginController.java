package hanium.project941s.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/loginForm")
    public String loginForm(Model model){
        return "loginForm";
    }
}
