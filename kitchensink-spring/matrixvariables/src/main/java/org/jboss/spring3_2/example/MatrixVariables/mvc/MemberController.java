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
package org.jboss.spring3_2.example.MatrixVariables.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.jboss.spring3_2.example.MatrixVariables.domain.Member;
import org.jboss.spring3_2.example.MatrixVariables.repo.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class MemberController {
    @Autowired
    private MemberDao memberDao;

    @RequestMapping(method = RequestMethod.GET)
    public String displaySortedMembers(Model model) {
        model.addAttribute("newMember", new Member());
        model.addAttribute("members", memberDao.findAllOrderedByName());
        return "index";
    }


    @RequestMapping(value = "/{filter}", method = RequestMethod.GET)
    public ModelAndView filteredMembers(@MatrixVariable(required = false, defaultValue = "") String n, @MatrixVariable(required = false, defaultValue = "") String e) {
        ModelAndView model = new ModelAndView("index");
        model.addObject("newMember", new Member());
        List<Member> members = memberDao.findByNameAndEmail(n, e);
        model.addObject("members", members);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerNewMember(@Valid @ModelAttribute("newMember") Member newMember, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            memberDao.register(newMember);
            return "redirect:/";
        } else {
            model.addAttribute("members", memberDao.findAllOrderedByName());
            return "index";
        }
    }
}
