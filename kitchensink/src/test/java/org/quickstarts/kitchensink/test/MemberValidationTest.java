/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.quickstarts.kitchensink.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.quickstarts.kitchensink.model.Member;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Member entity validation rules.
 * Tests all validation constraints without requiring a container.
 */
public class MemberValidationTest {

    private static Validator validator;
    private Member member;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        member = new Member();
        // Set valid defaults
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("1234567890");
    }

    @Test
    public void testValidMember() {
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(0, violations.size(), "Valid member should have no violations");
    }

    // Name validation tests
    @Test
    public void testNameCannotBeNull() {
        member.setName(null);
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Name cannot be null");
        ConstraintViolation<Member> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    public void testNameCannotBeEmpty() {
        member.setName("");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.size() > 0, "Empty name should be invalid");
    }

    @Test
    public void testNameCannotContainNumbers() {
        member.setName("John123");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Name cannot contain numbers");
        ConstraintViolation<Member> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Must not contain numbers", violation.getMessage());
    }

    @Test
    public void testNameCannotExceed25Characters() {
        member.setName("ThisNameIsWayTooLongAndExceedsTwentyFiveCharacters");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Name cannot exceed 25 characters");
        ConstraintViolation<Member> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    public void testNameCanBe25CharactersLong() {
        member.setName("JohnDoeWithTwentyFiveCh"); // exactly 25 characters
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(0, violations.size(), "Name with 25 characters should be valid");
    }

    @Test
    public void testNameCanBe1CharacterLong() {
        member.setName("J");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(0, violations.size(), "Name with 1 character should be valid");
    }

    // Email validation tests
    @Test
    public void testEmailCannotBeNull() {
        member.setEmail(null);
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.size() > 0, "Email cannot be null");
        boolean foundNotNull = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(foundNotNull, "Should have NotNull violation for email");
    }

    @Test
    public void testEmailCannotBeEmpty() {
        member.setEmail("");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.size() > 0, "Email cannot be empty");
    }

    @Test
    public void testEmailMustBeValidFormat() {
        member.setEmail("notanemail");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Invalid email format should be rejected");
        ConstraintViolation<Member> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    public void testEmailWithoutAtSymbolIsInvalid() {
        member.setEmail("johndoe.example.com");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Email without @ should be invalid");
    }

    @Test
    public void testEmailWithoutDomainIsInvalid() {
        member.setEmail("john@");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Email without domain should be invalid");
    }

    @Test
    public void testValidEmailFormats() {
        String[] validEmails = {
            "simple@example.com",
            "first.last@example.com",
            "user+tag@example.co.uk",
            "test_email@test-domain.com"
        };

        for (String email : validEmails) {
            member.setEmail(email);
            Set<ConstraintViolation<Member>> violations = validator.validate(member);
            assertEquals(0, violations.size(), "Email '" + email + "' should be valid");
        }
    }

    // Phone number validation tests
    @Test
    public void testPhoneNumberCannotBeNull() {
        member.setPhoneNumber(null);
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.size() > 0, "Phone number cannot be null");
        boolean foundNotNull = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber"));
        assertTrue(foundNotNull, "Should have NotNull violation for phoneNumber");
    }

    @Test
    public void testPhoneNumberMustBe10DigitsMinimum() {
        member.setPhoneNumber("123456789"); // 9 digits
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Phone number with less than 10 digits should be invalid");
    }

    @Test
    public void testPhoneNumberCanBe10Digits() {
        member.setPhoneNumber("1234567890"); // 10 digits
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(0, violations.size(), "Phone number with 10 digits should be valid");
    }

    @Test
    public void testPhoneNumberCanBe12Digits() {
        member.setPhoneNumber("123456789012"); // 12 digits
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(0, violations.size(), "Phone number with 12 digits should be valid");
    }

    @Test
    public void testPhoneNumberCannotExceed12Digits() {
        member.setPhoneNumber("1234567890123"); // 13 digits
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.size() > 0, "Phone number with more than 12 digits should be invalid");
    }

    @Test
    public void testPhoneNumberCannotContainLetters() {
        member.setPhoneNumber("12345abc90");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "Phone number with letters should be invalid");
    }

    @Test
    public void testPhoneNumberCannotContainSpecialCharacters() {
        member.setPhoneNumber("123-456-7890");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.size() > 0, "Phone number with special characters should be invalid");
    }

    @Test
    public void testPhoneNumberMustBeDigitsOnly() {
        member.setPhoneNumber("(123)456-7890");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.size() > 0, "Phone number with formatting should be invalid");
    }
}
