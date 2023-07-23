package hanium.project941s.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ServiceEnv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "env_id")
    private Long id; //기본키
    private String envKey;
    private String envValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private MemberService memberService;
}
