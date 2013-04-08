/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.spring3_2.example.AsyncRequestMapping.mvc;

import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.jboss.spring3_2.example.AsyncRequestMapping.domain.Member;
import org.jboss.spring3_2.example.AsyncRequestMapping.repo.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class MemberControllerDeferred {
    @Autowired
    private MemberDao memberDao;

    @RequestMapping(method = RequestMethod.GET)
    public String displaySortedMembers(Model model) {
        model.addAttribute("newMember", new Member());
        model.addAttribute("members", memberDao.findAllOrderedByName());
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public Callable<String> registerNewMember(@Valid @ModelAttribute("newMember") final Member newMember, final BindingResult result, final Model model) {

        return new Callable<String>() {
            public String call() {
                if (!result.hasErrors()) {
                    memberDao.register(newMember);
                    return "redirect:/";
                } else {
                    model.addAttribute("members", memberDao.findAllOrderedByName());
                    return "index";
                }
            }

        };
    }
}
