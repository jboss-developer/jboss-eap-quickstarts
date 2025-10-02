package org.jboss.as.quickstarts.kitchensink.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/members")
public class MemberResourceController {

    private final MemberRepository repository;
    private final MemberRegistration registration;

    public MemberResourceController(MemberRepository repository, MemberRegistration registration) {
        this.repository = repository;
        this.registration = registration;
    }

    @GetMapping(produces = "application/json")
    public List<Member> listAllMembers() {
        return repository.findAllByOrderByNameAsc();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Member> lookupMemberById(@PathVariable("id") long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member) {
        try {
            if (repository.existsByEmail(member.getEmail())) {
                Map<String, String> responseObj = new HashMap<>();
                responseObj.put("email", "Email taken");
                return ResponseEntity.status(409).body(responseObj);
            }
            registration.register(member);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            return ResponseEntity.status(409).body(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(responseObj);
        }
    }
}
