/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.quickstarts.kitchensink.rest;

import org.quickstarts.kitchensink.data.MemberRepository;
import org.quickstarts.kitchensink.model.Member;
import org.quickstarts.kitchensink.service.MemberRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring REST Controller for Member resources.
 * Migrated from JAX-RS to Spring MVC, and from JPA to MongoDB.
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members collection.
 */
@RestController
@RequestMapping("/rest/members")
public class MemberResourceRESTService {

    private static final Logger log = LoggerFactory.getLogger(MemberResourceRESTService.class);

    private final MemberRepository repository;
    private final MemberRegistration registration;

    public MemberResourceRESTService(MemberRepository repository, MemberRegistration registration) {
        this.repository = repository;
        this.registration = registration;
    }

    @GetMapping
    public List<Member> listAllMembers() {
        return repository.findAllByOrderByNameAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> lookupMemberById(@PathVariable("id") String id) {
        Member member = repository.findMemberById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member);
    }

    /**
     * Creates a new member from the values provided. Performs validation, and will return a response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member) {
        try {
            // Check the uniqueness of the email address
            if (emailAlreadyExists(member.getEmail())) {
                Map<String, String> responseObj = new HashMap<>();
                responseObj.put("email", "Email taken");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
            }

            registration.register(member);

            // Create an "ok" response
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(responseObj);
        }
    }

    /**
     * Exception handler for validation errors.
     * Spring automatically triggers this when @Valid fails on a @RequestBody parameter.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.debug("Validation completed. violations found: {}", errors.size());
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Checks if a member with the same email address is already registered.
     * Verifies the @Indexed(unique = true) constraint on the email field.
     *
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(String email) {
        Member member = repository.findByEmail(email);
        return member != null;
    }
}
