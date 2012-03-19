/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wicketstuff.javaee.example.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.wicketstuff.javaee.example.model.Contact;

/**
 * 
 * @author Filippo Diotalevi
 */
@Stateless
public class ContactDaoBean implements ContactDaoLocal
{

	@PersistenceContext
	private EntityManager em;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Contact> getContacts()
	{
		return em.createQuery("SELECT c FROM Contact c").getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getContact(Long id)
	{
		return em.find(Contact.class, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addContact(String name, String email)
	{
		em.merge(new Contact(null, name, email));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Contact modelObject)
	{
		Contact managed = em.merge(modelObject);
		em.remove(managed);
		em.flush();
	}
}
