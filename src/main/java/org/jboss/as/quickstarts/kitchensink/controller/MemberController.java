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
package org.jboss.as.quickstarts.kitchensink.controller;

import java.util.List;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@Controller
public class MemberController {

    private final MemberRegistration memberRegistration;
    private final MemberRepository memberRepository;

    public MemberController(MemberRegistration memberRegistration, MemberRepository memberRepository) {
        this.memberRegistration = memberRegistration;
        this.memberRepository = memberRepository;
    }

    @GetMapping({"/", "/index"})
    public String showForm(Model model) {
        model.addAttribute("newMember", new Member());
        populateMembers(model);
        return "index";
    }

    @PostMapping("/members")
    public String register(@Valid @ModelAttribute("newMember") Member newMember,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            populateMembers(model);
            return "index";
        }

        try {
            memberRegistration.register(newMember);
            model.addAttribute("successMessage", "Registration successful");
            populateMembers(model);
            model.addAttribute("newMember", new Member());
        } catch (ValidationException | DataIntegrityViolationException ex) {
            bindingResult.rejectValue("email", "duplicate", ex.getMessage());
            populateMembers(model);
        }
        return "index";
    }

    private void populateMembers(Model model) {
        List<Member> members = memberRepository.findAllByOrderByNameAsc();
        model.addAttribute("members", members);
    }
}
