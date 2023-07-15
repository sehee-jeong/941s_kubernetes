package hanium.project941s.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("admin/user")
    public String user(Model model){
        return "admin/user";
    }
}
