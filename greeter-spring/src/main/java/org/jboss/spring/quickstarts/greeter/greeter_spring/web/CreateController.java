/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.spring.quickstarts.greeter.greeter_spring.web;

import org.jboss.spring.quickstarts.greeter.greeter_spring.domain.User;
import org.jboss.spring.quickstarts.greeter.greeter_spring.domain.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("create")
public class CreateController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    void getInitialMessage() {
        // do nothing
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ModelAttribute("message")
    String create(User user) {
        try {
            userDao.createUser(user);
            return "A new user with id "+user.getId()+" has been created successfully";
        } catch (Exception e) {
            return "An error has occured while creating the user (see log for details)";
        }
    }
}
