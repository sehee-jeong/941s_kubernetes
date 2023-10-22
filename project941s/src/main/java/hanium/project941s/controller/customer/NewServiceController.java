package hanium.project941s.controller.customer;

import hanium.project941s.dto.NewServiceDTO;
import hanium.project941s.dto.PrincipalDetails;
import hanium.project941s.service.JenkinsService;
import hanium.project941s.service.MemberServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class NewServiceController {
    private final MemberServiceService memberServiceService;
    private final JenkinsService jenkinsService;

    @GetMapping("/customer/newService")
    public String newService(Model model){
        model.addAttribute("form", new NewServiceDTO());
        return "customer/newService";
    }

    @PostMapping("/customer/deploy")
    public String deployNewService(@AuthenticationPrincipal PrincipalDetails principal,
                                   @ModelAttribute("form") NewServiceDTO formDto,
                                   @RequestParam("keys") String[] keys,
                                   @RequestParam("values") String[] values,
                                   RedirectAttributes re) {
        // Port, Env 설정
        int port = formDto.getInnerPort();
        formDto.getEnv().put("port", new ArrayList<String>(Arrays.asList(String.valueOf(port))));
        for (int i = 0; i < keys.length; i++) {
            formDto.getEnv().put(keys[i], new ArrayList<String>(Arrays.asList(values[i])));
        }
        // serviceName 소문자로 변경
        String serviceName = formDto.getServiceName().toLowerCase();
        formDto.setServiceName(serviceName);
        // outterPort 설정
        int outterPort = memberServiceService.getUniquePort();
        formDto.setOutterPort(outterPort);

        // Job 생성
        String memberProviderId = principal.getMember().getMemberProviderId();
        if (jenkinsService.createJobToJenkins(formDto, memberProviderId) == false){
            return "customer/deployFail";
        }
        // DB 추가
        if (memberServiceService.createMemberService(formDto, memberProviderId) == false){
            return "customer/deployFail";
        }

        re.addAttribute("serviceName", formDto.getServiceName());

        return "redirect:/customer/buildViewer";
    }

    @GetMapping("/customer/buildViewer")
    public String test(@AuthenticationPrincipal PrincipalDetails principal,
                       @RequestParam("serviceName") String serviceName,
                       Model model){
        model.addAttribute("serviceName", serviceName);

        String customer = principal.getMember().getMemberProviderId().replaceAll("_", "-");
        String jenkinsSerivceName = customer + "-" + serviceName;
        String buildStatus = jenkinsService.checkServiceBuild(jenkinsSerivceName);
        model.addAttribute("buildStatus", buildStatus);

        String buildLog = jenkinsService.receiveServiceBuildLog(jenkinsSerivceName);
        model.addAttribute("buildLog", buildLog);
        return "customer/deploy";
    }
}
