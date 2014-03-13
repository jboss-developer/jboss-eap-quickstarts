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
package org.jboss.as.quickstarts.bean_validation_custom_constraint;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Simple tests for Bean Validator using custom constraints. Arquillian deploys an WAR archive to the application server, which
 * constructs Validator object.
 * 
 * This object is injected into the tests so user can verify the validators are working. This example does not touch validation
 * on database layer, e.g. it is not validating uniqueness constraint for email field.
 * 
 * 
 * @author <a href="https://community.jboss.org/people/giriraj.sharma27">Giriraj Sharma</a>
 * 
 */
@RunWith(Arquillian.class)
public class MyPersonTest {

    /**
     * Constructs a deployment archive
     * 
     * @return the deployment archive
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(Person.class, PersonAddress.class, Address.class, AddressValidator.class)
            // enable JPA
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            // add sample data
            .addAsResource("import.sql")
            // enable CDI
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            // Deploy our test datasource
            .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    // Get configured validator directly from JBoss EAP 6 environment
    @Inject
    Validator validator;

    /**
     * Tests an empty member registration, e.g. violation of:
     * 
     * <ul>
     * <li>@NotNull</li>
     * <li>@Size</li>
     * <li>@Address</li>
     * </ul>
     */

    /**
     * Tests an invalid Person registration
     */
    @Test
    public void testRegisterEmptyPerson() {

        Person person = new Person();
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        Assert.assertEquals("Four violations were found", 4, violations.size());
    }

    /**
     * Tests a valid Person registration
     */
    @Test
    public void testCorrectAddress() {
        Set<ConstraintViolation<Person>> violations = validator.validate(createValidPerson());

        Assert.assertEquals("No violations were found", 0, violations.size());
    }

    /**
     * Tests {@code @NotNull} constraint
     */
    @Test
    public void testFirstNameNullViolation() {
        Person person = createValidPerson();
        person.setFirstName(null);
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        Assert.assertEquals("One violation was found", 1, violations.size());
        Assert.assertEquals("First Name was invalid", "must be not null", violations.iterator().next()
            .getMessage());
    }

    /**
     * Tests {@code @Size} constraint
     */
    @Test
    public void testFirstNameSizeViolation() {
        Person person = createValidPerson();
        person.setFirstName("Lee");
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        Assert.assertEquals("One violation was found", 1, violations.size());
        Assert.assertEquals("First Name was invalid", "size must be at least four characters", violations.iterator().next()
            .getMessage());
    }

    /**
     * Validating the model data which has incorrect values. 
     * Tests {@code @Address} constraint
     */
    @Test
    public void testAddressViolation() {
        Person person = createValidPerson();
        // setting address itself as null
        person.setPersonAddress(null);
        validateAddressConstraints(person);

        // One of the address field is null.
        person.getPersonAddress().setCity(null);
        validateAddressConstraints(person);

        // Setting pin code less than 6 characters.
        person.getPersonAddress().setPinCode("123");
        person.getPersonAddress().setCity("Auckland");
        validateAddressConstraints(person);

        // Setting country name with less than 4 characters
        person.getPersonAddress().setPinCode("123456");
        person.getPersonAddress().setCountry("RIO");
        validateAddressConstraints(person);

    }

    private void validateAddressConstraints(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        for (ConstraintViolation<Person> violation : violations) {
            Assert.assertEquals("One violation was found", 1, violations.size());
            Assert.assertEquals("Address Field  was invalid", violation.getInvalidValue(), violation.getMessage());
        }
    }

    private Person createValidPerson() {
        PersonAddress address = new PersonAddress("#12, 4th Main", "XYZ Layout", "Bangalore", "Karnataka", "India", "56004554");
        Person person = new Person("John", "Smith", address);
        return person;
    }
}
