package org.jboss.as.quickstarts.kitchensink.controller;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.kitchensink.model.Member;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@ConversationScoped @Named
public class MembersController {

   @Inject
   private Logger log;

   @Inject
   private EntityManager em;

   @Inject
   private Event<Member> memberEventSrc;
   
   @Inject Conversation conversation;
   
   private Member member;
   
   private String email;

   @Produces
   @Named
   public Member getMember() {
      return member;
   }

   public void save() throws Exception {
      if (email == null) {
         log.info("Registering " + member.getName());
         em.persist(member);
         FacesContext.getCurrentInstance().addMessage("member", new FacesMessage(SEVERITY_INFO, "Registered " + member.getEmail(), null));
      } else {
         log.info("Updating " + member.getName());
         em.merge(member);
         FacesContext.getCurrentInstance().addMessage("member", new FacesMessage(SEVERITY_INFO, "Updated " + member.getEmail(), null));
         conversation.end();
      }
      init();
      memberEventSrc.fire(member);
   }
   
   @PostConstruct
   public void init() {
      this.email = null;
      this.member = new Member();
   }
   
   public String getEmail() {
      return email;
   }
   
   public void setEmail(String id) {
      this.email = id;
   }
   
   public void retrieve() {
      if (email != null) {
         if (conversation.isTransient()) {
            conversation.begin();
         }
         List<Member> members = em.createQuery("select m from Member m where m.email = :email").setParameter("email", email).getResultList();
         if (members.isEmpty()) {
            // This could happen if the user manually sets the wrong email addr
            FacesContext.getCurrentInstance().addMessage("member", new FacesMessage(SEVERITY_ERROR, "Unable to load member with email " + email, null));
         } else if (members.size() > 1) {
            // This should never happen, it means a db constraint went wrong
            throw new IllegalStateException("Something went very wrong, email should be unique!");
         } else {
            this.member = members.get(0);
         }
      }
   }
   
   public void remove() {
      if (email != null) {
         log.info("Removing " + member.getName());
         List<Member> members = em.createQuery("select m from Member m where m.email = :email").setParameter("email", email).getResultList();
         if (members.isEmpty()) {
            // This could happen if the user manually sets the wrong email addr
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_ERROR, "Unable to load member with email " + email, null));
         } else if (members.size() > 1) {
            // This should never happen, it means a db constraint went wrong
            throw new IllegalStateException("Something went very wrong, email should be unique!");
         } else {
            this.member = members.get(0);
            em.remove(member);
            FacesContext.getCurrentInstance().addMessage("member", new FacesMessage(SEVERITY_INFO, "Deleted " + member.getEmail(), null));
            memberEventSrc.fire(member);
            conversation.end();
         }
         init();
      }
   }
}
