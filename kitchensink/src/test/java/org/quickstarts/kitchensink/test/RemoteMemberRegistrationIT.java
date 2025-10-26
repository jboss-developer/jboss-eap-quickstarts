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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.quickstarts.kitchensink.data.MemberRepository;
import org.quickstarts.kitchensink.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;

/**
 * Spring Boot integration test for member registration.
 *
 * This test automatically:
 * - Starts embedded MongoDB (via Flapdoodle)
 * - Starts the Spring Boot application on a random port
 * - Configures TestRestTemplate for HTTP calls
 * - Cleans up MongoDB data between tests
 *
 * No manual setup required - just run the test!
 */
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = {EmbeddedMongoAutoConfiguration.class}
)
public class RemoteMemberRegistrationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository repository;

    /**
     * Configure embedded MongoDB to use a random port for tests.
     * This allows multiple tests to run in parallel without port conflicts.
     */
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // Embedded MongoDB will use default settings
        // Spring Boot auto-configures it via Flapdoodle
    }

    /**
     * Clean up the database before each test to ensure test isolation.
     */
    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void testRegister() {
        // Arrange: Create a new member
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

        // Act: POST to registration endpoint
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/rest/members",
            newMember,
            String.class
        );

        // Assert: Registration should succeed
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Registration should succeed");

        // Verify the member was actually saved to MongoDB
        Member savedMember = repository.findByEmail("jane@mailinator.com");
        assertNotNull(savedMember, "Member should be saved in database");
        assertEquals("Jane Doe", savedMember.getName());
        assertEquals("2125551234", savedMember.getPhoneNumber());
    }

    @Test
    public void testRegisterDuplicateEmail() {
        // Arrange: Create and save first member
        Member firstMember = new Member();
        firstMember.setName("John Smith");
        firstMember.setEmail("duplicate@example.com");
        firstMember.setPhoneNumber("1234567890");
        repository.save(firstMember);

        // Arrange: Try to register another member with same email
        Member duplicateMember = new Member();
        duplicateMember.setName("Jane Smith");
        duplicateMember.setEmail("duplicate@example.com");
        duplicateMember.setPhoneNumber("0987654321");

        // Act: POST duplicate email
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/rest/members",
            duplicateMember,
            String.class
        );

        // Assert: Should return 409 Conflict
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode(),
            "Duplicate email should return 409 Conflict");
    }
}
