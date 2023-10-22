package hanium.project941s.domain;

import hanium.project941s.domain.Enums.ServiceStatus;
import hanium.project941s.dto.NewServiceDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MemberService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id; //기본키
    private String name; //서비스 이름
    private String version; //서비스 버전
    private String githubUrl; //github url
    private String url; //deploy url
    private int port; // 외부 개방 포트
    private Date date; //수정일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus serviceStatus; // Success, FAILURE 타입

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "memberService", cascade = CascadeType.ALL)
    private List<ServiceEnv> serviceEnvs = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<MemberAct> memberActs = new ArrayList<>();

    @Builder
    public MemberService(String name, String githubUrl, String url, Date date, ServiceStatus serviceStatus, String version, int port) {
        this.name = name;
        this.githubUrl = githubUrl;
        this.url = url;
        this.date = date;
        this.serviceStatus = serviceStatus;
        this.version = version;
        this.port = port;
    }

    //==연관관계 메서드==//
    public void setMember(Member member, MemberAct act) {
        this.member = member;
        member.getMemberServices().add(this);
    }
    public void addAct(MemberAct act) {
        this.memberActs.add(act);
        act.setService(this);
    }

    //==생성 메서드==//
    public static MemberService createMemberService(String EksUrl, NewServiceDTO newServiceDTO, Member member, MemberAct memberAct){
        MemberService memberService = MemberService.builder()
                .name(newServiceDTO.getServiceName())
                .githubUrl(newServiceDTO.getGithubUrl())
                .url(EksUrl + newServiceDTO.getOutterPort())
                .date(new Date())
                .serviceStatus(ServiceStatus.SUCCESS)
                .version(memberAct.getVersion())
                .port(newServiceDTO.getOutterPort())
                .build();

        member.addAct(memberAct);
        memberService.setMember(member, memberAct);
        memberService.addAct(memberAct);

        return memberService;
    }
}
