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
package org.jboss.quickstarts.wfk.contact;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.util.ConvertDate;

import java.util.List;
import java.util.logging.Logger;

/**
 * This Service assumes the Control responsibility in the ECB pattern. 
 * 
 * The validation is done here so that it may be used by other Boundary Resources.  Other Business Logic would go here
 * as well. 
 * 
 * There are no access modifiers on the methods making them 'package' scope.  They should only be accessed by a 
 * Boundary / Web Service class with public methods. 
 * 
 * @author Joshua Wilson
 *
 */
// The @Dependent is the default scope is listed here so that you know what scope is being used.
@Dependent
public class ContactService {

    @Inject
    private Logger log;

    @Inject
    private ContactValidator validator;

    @Inject
    private ContactRepository crud;
    
    /**
     * Find all the Contacts and sort them alphabetically by last name.
     * 
     * @return List of Contacts
     */
    List<Contact> findAllOrderedByName() {
        List<Contact> contacts = crud.findAllOrderedByName();
        return contacts;
    }

    /**
     * Find just one Contact by it's ID.
     * 
     * @param id
     * @return Contact
     */
    Contact findById(Long id) {
        Contact contact = crud.findById(id);
        return contact;
    }

    /**
     * Find just one Contact by the email that is passed in. If there is more then one, only the first will be returned.
     * 
     * @param email
     * @return Contact
     */
    Contact findByEmail(String email) {
        Contact contact = crud.findByEmail(email);
        return contact;
    }

    /**
     * Find just one Contact by the first name that is passed in. If there is more then one, only the first will be returned.
     * 
     * @param firstName
     * @return Contact
     */
    Contact findByFirstName(String firstName) {
        Contact contact = crud.findByFirstName(firstName);
        return contact;
    }

    /**
     * Find just one Contact by the last name that is passed in. If there is more then one, only the first will be returned.
     * 
     * @param lastName
     * @return Contact
     */
    Contact findByLastName(String lastName) {
        Contact contact = crud.findByFirstName(lastName);
        return contact;
    }

    /**
     * Create a Contact and store it in the database.
     * 
     * Validate the data in the Contact.
     * 
     * @param Contact
     * @return Contact
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Contact create(Contact contact) throws ConstraintViolationException, ValidationException, Exception {
        log.info("ContactService.create() - Creating " + contact.getFirstName() + " " + contact.getLastName());
        
        /*
         * There is a problem with the dates as the come back from the UI. They are affected by a local vs UTC change that
         * in some cases causes the date to lose a day every time they are saved.
         * 
         * The real root of the problem appears to be when a date with a time stamp in the local(which for any timezone 
         * west of GMT will subtract at least 1 hour causing the date to be subtracted too) is saved in the H2 database,
         * the database does not convert it to UTC it saves the local time with no timezone. To work around this we obtain
         * the timezone offset and subtract that from the date with local time. This at least brings us closer to UTC. In order 
         * to deal with any slight variations in the offset calculations of java we set the time of the 'Birthdate' to 
         * 12:00.  This means that even if the time shifts one way or another it does not change the day.
         * 
         * If a more robust database were used this would not be a problem. Please keep this in mind if you use this code base. 
         */
        contact.setBirthDate(ConvertDate.localToGMT(contact.getBirthDate()));

        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
        validator.validateContact(contact);
        
        // Write the contact to the database.
        Contact createdContact = crud.create(contact);
        
        return createdContact;
    }

    /**
     * Update a Contact in the database.
     * 
     * Validate the data in the Contact.
     * 
     * @param Contact
     * @return Contact
     * @throws ConstraintViolationException, ValidationException, Exception
     */
//    Map<String, Object> update(Contact contact) throws Exception {
    Contact update(Contact contact) throws ConstraintViolationException, ValidationException, Exception {
        log.info("ContactService.update() - Updating " + contact.getFirstName() + " " + contact.getLastName());
        
        /*
         * There is a problem with the dates as the come back from the UI. They are affected by a local vs UTC change that
         * in some cases causes the date to lose a day every time they are saved.
         * 
         * The real root of the problem appears to be when a date with a time stamp in the local(which for any timezone 
         * west of GMT will subtract at least 1 hour causing the date to be subtracted too) is saved in the H2 database,
         * the database does not convert it to UTC it saves the local time with no timezone. To work around this we obtain
         * the timezone offset and subtract that from the date with local time. This at least brings us closer to UTC. In order 
         * to deal with any slight variations in the offset calculations of java we set the time of the 'Birthdate' to 
         * 12:00.  This means that even if the time shifts one way or another it does not change the day.
         * 
         * If a more robust database were used this would not be a problem. Please keep this in mind if you use this code base. 
         */
        contact.setBirthDate(ConvertDate.localToGMT(contact.getBirthDate()));
        
        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
        validator.validateContact(contact);

        // Either update the contact or add it if it can't be found.
        Contact updatedContact = crud.update(contact);
        
        return updatedContact;
    }

    /**
     * Delete a Contact in the database.
     * 
     * @param Contact
     * @return Contact
     * @throws Exception
     */
//    Map<String, Object> delete(Contact contact) throws Exception {
    Contact delete(Contact contact) throws Exception {
        log.info("ContactService.delete() - Deleting " + contact.getFirstName() + " " + contact.getLastName());
        
        Contact deletedContact = null;
        
        if (contact.getId() != null) {
            deletedContact = crud.delete(contact);
        } else {
            log.info("ContactService.delete() - No ID was found so can't Delete.");
        }
        
        return deletedContact;
    }

}
