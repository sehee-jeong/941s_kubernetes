package hanium.project941s.repository;

import hanium.project941s.domain.MemberService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<MemberService, Long> {

    public MemberService getOne(Long id);

    public List<MemberService> findByMember_Id(Long id);
}
