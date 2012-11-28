/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.controller;

import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.AuditContact;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.Contact;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence.AuditRepository;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence.ContactRepository;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Named
@ConversationScoped
public class ContactController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FacesContext facesContext;

    @Inject
    private ContactRepository contactRepository;

    @Inject
    private AuditRepository auditRepository;

    @Inject
    private Conversation conversation;

    private Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void save() {
        String msg = contact.getId() == null ? "New Contact added" : "Contact updated";
        try {
            contactRepository.persist(contact);
        } catch (Exception e) {
            conversation.end();
            msg = e.getMessage();
        }
        facesContext.addMessage(null, new FacesMessage(msg));
    }

    public void remove(Contact contact) {
        contactRepository.remove(contact);
        facesContext.addMessage(null, new FacesMessage("Contact Removed"));
        this.contact = new Contact();
    }

    public void selectForEdit(Contact contact) {
        this.contact = contact;
    }

    @PostConstruct
    public void newContact() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        contact = new Contact();
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    public String getConversationNumber() {
        return "Conversation Id: " + conversation.getId();
    }

    @Produces
    @Named
    @RequestScoped
    public List<Contact> getAllContacts() {
        return contactRepository.getAllContacts();
    }

    @Produces
    @Named
    public List<AuditContact> getAuditRecords() {
        return auditRepository.getAllAuditRecords();
    }

}
