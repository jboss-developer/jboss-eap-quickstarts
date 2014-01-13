/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.quickstarts.wfk.service;

import org.jboss.quickstarts.wfk.model.Member;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author Joshua Wilson
 *
 */
// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Member> memberEventSrc;

    /**
     * Persist takes an entity instance, adds it to the context and makes that instance managed (ie future updates 
     * to the entity will be tracked)
     * 
     * persist() will set the @GeneratedValue @Id for an object. 
     */
    public Member create(Member member) throws Exception {
        log.info("Creating " + member.getFirstName() + " " + member.getLastName());
        em.persist(member);
        memberEventSrc.fire(member);
        return member;
    }

    /**
     * Merge creates a new instance of your entity, copies the state from the supplied entity, and makes the new 
     * copy managed. The instance you pass in will not be managed (any changes you make will not be part of the 
     * transaction - unless you call merge again).
     * 
     * merge() however must have an object with the @Id already generated.
     */
    public Member update(Member member) throws Exception {
        log.info("Updating " + member.getFirstName() + " " + member.getLastName());
        em.merge(member);
        memberEventSrc.fire(member);
        return member;
    }

    public Member delete(Member member) throws Exception {
        log.info("Deleting " + member.getFirstName() + " " + member.getLastName());
        em.remove(em.merge(member));
        memberEventSrc.fire(member);
        return member;
    }
}
