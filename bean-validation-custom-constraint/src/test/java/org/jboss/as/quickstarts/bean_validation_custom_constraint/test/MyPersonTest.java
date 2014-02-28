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
package org.jboss.as.quickstarts.bean_validation_customConstraint;

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
        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(MyPerson.class)
            .addClasses(MyAddress.class).addClasses(Address.class).addClasses(AddressValidator.class)
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

        MyPerson person = new MyPerson();
        Set<ConstraintViolation<MyPerson>> violations = validator.validate(person);

        Assert.assertEquals("Five violations were found", 5, violations.size());
    }

    /**
     * Tests a valid Person registration
     */
    @Test
    public void testCorrectAddress() {
        Set<ConstraintViolation<MyPerson>> violations = validator.validate(createValidPerson());

        Assert.assertEquals("No violations were found", 0, violations.size());
    }

    /**
     * Tests {@code @NotNull} constraint
     */
    @Test
    public void testFirstNameNullViolation() {
        MyPerson person = createValidPerson();
        person.setFirstName(null);
        Set<ConstraintViolation<MyPerson>> violations = validator.validate(person);

        Assert.assertEquals("One violation was found", 1, violations.size());
        Assert.assertEquals("First Name was invalid", "must be not null", violations.iterator().next()
            .getMessage());
    }

    /**
     * Tests {@code @Size} constraint
     */
    @Test
    public void testFirstNameSizeViolation() {
        MyPerson person = createValidPerson();
        person.setFirstName("Lee");
        Set<ConstraintViolation<MyPerson>> violations = validator.validate(person);

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
        MyPerson person = createValidPerson();
        // setting address itself as null
        person.setAddress(null);
        validateAddressConstraints(person);

        // One of the address field is null.
        person.getAddress().setCity(null);
        validateAddressConstraints(person);

        // Setting pin code less than 6 characters.
        person.getAddress().setPinCode("123");
        person.getAddress().setCity("Auckland");
        validateAddressConstraints(person);

        // Setting country name with less than 4 characters
        person.getAddress().setPinCode("123456");
        person.getAddress().setCountry("RIO");
        validateAddressConstraints(person);

    }

    private void validateAddressConstraints(MyPerson person) {
        Set<ConstraintViolation<MyPerson>> violations = validator.validate(person);

        for (ConstraintViolation<MyPerson> violation : violations) {
            Assert.assertEquals("One violation was found", 1, violations.size());
            Assert.assertEquals("Address Field  was invalid", violation.getInvalidValue(), violation.getMessage());
        }
    }

    private MyPerson createValidPerson() {
        MyAddress address = new MyAddress("#12, 4th Main", "XYZ Layout", "Bangalore", "Karnataka", "India", "56004554");
        MyPerson person = new MyPerson("John", "Smith", address);
        return person;
    }
}
