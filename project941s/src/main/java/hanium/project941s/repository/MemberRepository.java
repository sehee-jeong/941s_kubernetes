package hanium.project941s.repository;


import hanium.project941s.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member getOne(Long id);
    public List<Member> findByMemberProviderId(String MemberProviderId);

    public Member findMemberByMemberProviderId(String MemberProviderId);
}
