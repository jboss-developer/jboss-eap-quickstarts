package org.jboss.as.quickstarts.kitchensink.service;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberRegistration {

    private final MemberRepository repository;

    public MemberRegistration(MemberRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Member register(Member member) {
        return repository.save(member);
    }
}
