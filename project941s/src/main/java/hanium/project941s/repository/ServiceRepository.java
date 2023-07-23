package hanium.project941s.repository;

import hanium.project941s.domain.MemberService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<MemberService, Long> {
}
