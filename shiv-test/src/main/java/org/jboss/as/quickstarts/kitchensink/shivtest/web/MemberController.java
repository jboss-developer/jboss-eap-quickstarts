package org.jboss.as.quickstarts.kitchensink.shivtest.web;

import java.net.URI;
import java.util.List;
import org.jboss.as.quickstarts.kitchensink.shivtest.model.Member;
import org.jboss.as.quickstarts.kitchensink.shivtest.service.MemberService;
import org.jboss.as.quickstarts.kitchensink.shivtest.web.dto.MemberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Validated
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping
    public List<Member> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> get(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Member> create(@RequestBody @Validated MemberRequest request) {
        Member toSave = Member.builder()
            .name(request.getName())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .build();
        Member saved = service.register(toSave);
        return ResponseEntity.created(URI.create("/api/members/" + saved.getId())).body(saved);
    }
}
