package org.jboss.as.quickstart.hibernate4.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.hibernate.Session;

import org.jboss.as.quickstart.hibernate4.model.Member;

/**
 * @author Madhumita Sadhukhan
 */
// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class MemberRegistration {

   @Inject
   private Logger log;

   @Inject
   private EntityManager em;
   
   @Inject
   private Event<Member> memberEventSrc;
   
   private Member newMember;

   @Produces
   @Named
   public Member getNewMember() {
      return newMember;
   }

   @Produces
   public void register() throws Exception {
      log.info("Registering " + newMember.getName());
      
      //using Hibernate session(Native API) and JPA entitymanager
      Session session = (Session)em.getDelegate();
      session.persist(newMember);
     
      try
      {
      memberEventSrc.fire(newMember);
      }
      catch(Exception e)
      {
    	  log.info("Registration Failed!You have provided duplicate details for Member!!!");
    	  e.printStackTrace();
      }
      
      
    	  
      initNewMember();
   }

   @PostConstruct
   public void initNewMember() {
      newMember = new Member();
   }
}
