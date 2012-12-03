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

package org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.Contact;

/**
 * This class is {@link Stateful} because we need to keep the {@link EntityManager} opened during the conversation scope.
 * 
 * @see ConversationScoped
 * 
 *      While the {@link EntityManager} is opened the {@link Contact} is managed by this entity and there's no need to
 *      constantly check the database (every request) to determine if this entity should be updated.
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Stateful
@ConversationScoped
public class ContactRepository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void persist(Contact contact) {
        entityManager.persist(contact);
        entityManager.flush();
    }

    public void remove(Contact contact) {
        entityManager.remove(contact);
        entityManager.flush();
    }

    @SuppressWarnings("unchecked")
    public List<Contact> getAllContacts() {
        return entityManager.createQuery("SELECT m From Contact m").getResultList();
    }

}
