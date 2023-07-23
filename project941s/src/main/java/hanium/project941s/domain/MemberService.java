package hanium.project941s.domain;

import hanium.project941s.domain.Enums.ActType;
import hanium.project941s.domain.Enums.ServiceStatus;
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
    private Date date; //수정일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus serviceStatus; // Success, Fail 타입

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "memberService", cascade = CascadeType.ALL)
    private List<ServiceEnv> serviceEnvs = new ArrayList<>();

    @Builder
    public MemberService(String name, String version, Date date) {
        this.name = name;
        this.version = version;
        this.date = date;
    }
}
