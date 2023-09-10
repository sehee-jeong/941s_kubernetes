package hanium.project941s.domain;

import hanium.project941s.domain.Enums.ActType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberAct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberAct_id")
    private Long id; //기본키
    private String version; //버전

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private MemberService service;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sampleDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActType actType; // CRUD 타입


    @Builder
    public MemberAct(String version, Date sampleDate, ActType actType) {
        this.version = version;
        this.sampleDate = sampleDate;
        this.actType = actType;
    }
}
