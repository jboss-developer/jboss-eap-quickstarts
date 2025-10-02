package org.jboss.as.quickstarts.kitchensink.data;

import java.util.List;
import java.util.Optional;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Member> findAllByOrderByNameAsc();
}
