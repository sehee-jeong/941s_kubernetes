package hanium.project941s.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MonitorController {
    @RequestMapping("monitor")
    public String monitor(Model model){
        return "admin/monitor";
    }
}
