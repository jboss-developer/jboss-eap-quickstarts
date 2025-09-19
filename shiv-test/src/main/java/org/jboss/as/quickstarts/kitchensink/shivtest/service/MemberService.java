package org.jboss.as.quickstarts.kitchensink.shivtest.service;

import java.util.List;
import java.util.Optional;
import org.jboss.as.quickstarts.kitchensink.shivtest.model.Member;
import org.jboss.as.quickstarts.kitchensink.shivtest.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public List<Member> findAll() {
        return repository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Member register(Member member) {
        repository.findByEmail(member.getEmail()).ifPresent(existing -> {
            throw new DuplicateEmailException(existing.getEmail());
        });
        return repository.save(member);
    }
}
