package org.jboss.as.quickstarts.kitchensink.shivtest.repository;

import java.util.Optional;
import org.jboss.as.quickstarts.kitchensink.shivtest.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
