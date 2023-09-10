package hanium.project941s.controller.customer;

import hanium.project941s.domain.MemberService;
import hanium.project941s.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyServicesController {
    private final ServiceRepository serviceRepository;

    @GetMapping("/customer/myServices")
    public String MyServices(Model model, @PageableDefault(size = 10) Pageable pageable){

        Page<MemberService> memberServices = serviceRepository.findAll(pageable);

//        int startPage = Math.max(1, userServices.getPageable().getPageNumber() - 4);
//        int minEndPage = Math.max(1, userServices.getTotalPages());
//        int endPage = Math.min(minEndPage, userServices.getPageable().getPageNumber() + 4);

        int startPage = 1;
        int endPage = Math.max(1, memberServices.getTotalPages());

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("userServices", memberServices);

//        model.addAttribute("deleteServices", new List<Integer>());

        return "customer/myServices";
    }

    @Transactional
    @PostMapping("/customer/deleteService")
    public String deleteService(@RequestParam("_selected_") String[] deleteServices){
        for (String str : deleteServices){
            serviceRepository.deleteById(Long.parseLong(str));
        }
        return "redirect:/customer/myServices";
    }
}
