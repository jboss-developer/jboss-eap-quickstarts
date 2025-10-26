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

import java.util.Map;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quickstarts.kitchensink.data.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for duplicate email handling.
 * Tests that the system properly prevents duplicate email registrations.
 *
 * This test automatically starts embedded MongoDB and the Spring Boot application.
 * No manual setup required!
 */
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = {EmbeddedMongoAutoConfiguration.class}
)
public class DuplicateEmailIT {

    private static final Logger log = Logger.getLogger(DuplicateEmailIT.class.getName());

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void testDuplicateEmailRejected() {
        String duplicateEmail = "duplicate.test@example.com";

        // First registration - should succeed
        Map<String, String> member1 = Map.of(
                "name", "First User",
                "email", duplicateEmail,
                "phoneNumber", "1234567890"
        );

        ResponseEntity<String> response1 = restTemplate.postForEntity("/rest/members", member1, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode(), "First registration should succeed");
        log.info("First registration succeeded for: " + duplicateEmail);

        // Second registration with same email - should fail
        Map<String, String> member2 = Map.of(
                "name", "Second User",
                "email", duplicateEmail,
                "phoneNumber", "0987654321"
        );

        ResponseEntity<Map> response2 = restTemplate.postForEntity("/rest/members", member2, Map.class);

        // Verify duplicate is rejected with 409 Conflict
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode(), "Duplicate email should return 409 Conflict");
        assertNotNull(response2.getBody(), "Response body should not be null");

        // Verify error message mentions email
        Map<String, String> errorResponse = response2.getBody();
        assertTrue(errorResponse.containsKey("email"), "Error response should contain 'email' field");
        String emailError = errorResponse.get("email");
        assertTrue(emailError.toLowerCase().contains("taken") || emailError.toLowerCase().contains("exists"),
                "Error message should mention 'taken' or similar");

        log.info("Successfully rejected duplicate email with 409 Conflict: " + duplicateEmail);
    }

    @Test
    public void testDuplicateEmailWithDifferentCase() {
        String baseEmail = "case.sensitive@example.com";

        // First registration with lowercase
        Map<String, String> member1 = Map.of(
                "name", "Lowercase User",
                "email", baseEmail,
                "phoneNumber", "1112223333"
        );

        ResponseEntity<String> response1 = restTemplate.postForEntity("/rest/members", member1, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode(), "First registration should succeed");

        // Second registration with different case
        String upperCaseEmail = baseEmail.toUpperCase();
        Map<String, String> member2 = Map.of(
                "name", "Uppercase User",
                "email", upperCaseEmail,
                "phoneNumber", "4445556666"
        );

        ResponseEntity<String> response2 = restTemplate.postForEntity("/rest/members", member2, String.class);

        // This test documents the current behavior - whether case-sensitive or not
        // If the database constraint is case-insensitive, this should return 409
        // If case-sensitive, this should return 200
        log.info("Registration with different case returned status: " + response2.getStatusCode());

        // For now, we just document the behavior without asserting
        // During migration, you may want to enforce case-insensitive email uniqueness
        if (response2.getStatusCode() == HttpStatus.CONFLICT) {
            log.info("System enforces case-insensitive email uniqueness");
        } else if (response2.getStatusCode() == HttpStatus.OK) {
            log.info("System allows different cases as different emails");
        }
    }

    @Test
    public void testMultipleUsersWithUniqueEmails() {
        // Test that multiple users can register with different emails
        String email1 = "unique1@example.com";
        String email2 = "unique2@example.com";
        String email3 = "unique3@example.com";

        // Register first user
        Map<String, String> member1 = Map.of(
                "name", "User One",
                "email", email1,
                "phoneNumber", "1231231234"
        );
        ResponseEntity<String> response1 = restTemplate.postForEntity("/rest/members", member1, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode(), "First user should register successfully");

        // Register second user
        Map<String, String> member2 = Map.of(
                "name", "User Two",
                "email", email2,
                "phoneNumber", "4564564567"
        );
        ResponseEntity<String> response2 = restTemplate.postForEntity("/rest/members", member2, String.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode(), "Second user should register successfully");

        // Register third user
        Map<String, String> member3 = Map.of(
                "name", "User Three",
                "email", email3,
                "phoneNumber", "7897897890"
        );
        ResponseEntity<String> response3 = restTemplate.postForEntity("/rest/members", member3, String.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode(), "Third user should register successfully");

        log.info("Successfully registered 3 users with unique emails");
    }

    @Test
    public void testDuplicateEmailErrorMessageFormat() {
        String duplicateEmail = "format.test@example.com";

        // First registration
        Map<String, String> member1 = Map.of(
                "name", "Original User",
                "email", duplicateEmail,
                "phoneNumber", "5556667777"
        );

        restTemplate.postForEntity("/rest/members", member1, String.class);

        // Second registration with duplicate email
        Map<String, String> member2 = Map.of(
                "name", "Duplicate User",
                "email", duplicateEmail,
                "phoneNumber", "8889990000"
        );

        ResponseEntity<Map> response2 = restTemplate.postForEntity("/rest/members", member2, Map.class);

        // Verify error response format
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode(), "Should return 409 Conflict");

        Map<String, String> errorResponse = response2.getBody();

        // Verify it's a proper JSON object with email field
        assertNotNull(errorResponse, "Response body should not be null");
        assertTrue(errorResponse.containsKey("email"), "Should be a JSON object with 'email' field");
        assertTrue(errorResponse.get("email") instanceof String, "Email field should be a string");
        assertTrue(!errorResponse.get("email").isEmpty(), "Error message should not be empty");

        log.info("Duplicate email error format verified: " + errorResponse);
    }
}
