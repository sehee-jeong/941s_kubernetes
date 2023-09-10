package hanium.project941s.domain;


import hanium.project941s.domain.Enums.Role;
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
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; //기본키
    private String name; //유저 이름
    private String memberProviderId; // 공급자_공급 아이디
    private String provider; //공급자 (google, github ...)
    private String providerId; //공급 아이디

    private Date date; // 최초 등록일
    private Date latestDate; // 최근 접속일

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberService> memberServices = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberAct> memberActs = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //유저 권한 (관리자, 고객)

    @Builder
    public Member(String name, String memberProviderId, Role role, String provider, String providerId, Date date, Date latestDate) {
        this.name = name;
        this.memberProviderId = memberProviderId;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.date = date;
        this.latestDate = latestDate;
    }

    //==연관관계 메서드==//
    public void addAct(MemberAct act) {
        this.memberActs.add(act);
        act.setMember(this);
    }
}
