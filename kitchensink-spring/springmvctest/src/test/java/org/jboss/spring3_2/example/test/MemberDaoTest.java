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
package org.jboss.spring3_2.example.test;

import java.util.List;

import junit.framework.Assert;

import org.jboss.spring3_2.example.domain.Member;
import org.jboss.spring3_2.example.repo.MemberDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml",
        "classpath:/META-INF/spring/applicationContext.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class MemberDaoTest {
    @Autowired
    private MemberDao memberDao;

    @Test
    public void testFindById() {
        Member member = memberDao.findById(0l);

        Assert.assertEquals("John Smith", member.getName());
        Assert.assertEquals("john.smith@mailinator.com", member.getEmail());
        Assert.assertEquals("2125551212", member.getPhoneNumber());
        return;
    }

    @Test
    public void testFindByEmail() {
        Member member = memberDao.findByEmail("john.smith@mailinator.com");

        Assert.assertEquals("John Smith", member.getName());
        Assert.assertEquals("john.smith@mailinator.com", member.getEmail());
        Assert.assertEquals("2125551212", member.getPhoneNumber());
        return;
    }

    @Test
    public void testRegister() {
        Member member = new Member();
        member.setEmail("jane.doe@mailinator.com");
        member.setName("Jane Doe");
        member.setPhoneNumber("2125552121");

        memberDao.register(member);
        Long id = member.getId();
        Assert.assertNotNull(id);

        Assert.assertEquals(2, memberDao.findAllOrderedByName().size());
        Member newMember = memberDao.findById(id);

        Assert.assertEquals("Jane Doe", newMember.getName());
        Assert.assertEquals("jane.doe@mailinator.com", newMember.getEmail());
        Assert.assertEquals("2125552121", newMember.getPhoneNumber());
        return;
    }

    @Test
    public void testFindAllOrderedByName() {
        Member member = new Member();
        member.setEmail("jane.doe@mailinator.com");
        member.setName("Jane Doe");
        member.setPhoneNumber("2125552121");
        memberDao.register(member);

        List<Member> members = memberDao.findAllOrderedByName();
        Assert.assertEquals(2, members.size());
        Member newMember = members.get(0);

        Assert.assertEquals("Jane Doe", newMember.getName());
        Assert.assertEquals("jane.doe@mailinator.com", newMember.getEmail());
        Assert.assertEquals("2125552121", newMember.getPhoneNumber());
        return;
    }
}
