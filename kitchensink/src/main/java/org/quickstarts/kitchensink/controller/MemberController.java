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
package org.quickstarts.kitchensink.controller;

import org.quickstarts.kitchensink.data.MemberRepository;
import org.quickstarts.kitchensink.model.Member;
import org.quickstarts.kitchensink.service.MemberRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Spring MVC Controller for the web UI.
 * Replaces the JSF managed bean with Thymeleaf views.
 */
@Controller
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberRepository repository;
    private final MemberRegistration registration;

    public MemberController(MemberRepository repository, MemberRegistration registration) {
        this.repository = repository;
        this.registration = registration;
    }

    @ModelAttribute("newMember")
    public Member newMember() {
        return new Member();
    }

    @GetMapping("/")
    public String index(Model model) {
        // Get all members to display in the table
        List<Member> members = repository.findAllByOrderByNameAsc();
        model.addAttribute("members", members);

        return "index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newMember") Member member,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Re-populate the members list for display
            List<Member> members = repository.findAllByOrderByNameAsc();
            model.addAttribute("members", members);
            return "index";
        }

        // Check for duplicate email
        if (emailAlreadyExists(member.getEmail())) {
            bindingResult.rejectValue("email", "error.member", "Email already taken");
            List<Member> members = repository.findAllByOrderByNameAsc();
            model.addAttribute("members", members);
            return "index";
        }

        try {
            registration.register(member);
            log.info("Member registered successfully: {}", member.getName());
            redirectAttributes.addFlashAttribute("message", "Member registered successfully!");
            return "redirect:/";
        } catch (Exception e) {
            log.error("Error registering member", e);
            bindingResult.reject("error.registration", "An error occurred during registration");
            List<Member> members = repository.findAllByOrderByNameAsc();
            model.addAttribute("members", members);
            return "index";
        }
    }

    private boolean emailAlreadyExists(String email) {
        Member member = repository.findByEmail(email);
        return member != null;
    }
}
