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

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for REST GET endpoints.
 * Tests member listing and lookup operations via HTTP.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RemoteMemberRESTServiceIT {

    private static final Logger log = Logger.getLogger(RemoteMemberRESTServiceIT.class.getName());

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testListAllMembers() {
        // First, create a test member to ensure there's at least one in the database
        createTestMember("John Doe", "john.doe.list@example.com", "5551234567");

        // Test: GET /rest/members
        ResponseEntity<List<Member>> response = restTemplate.exchange(
                "/rest/members",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Member>>() {}
        );

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        List<Member> members = response.getBody();
        assertTrue(members.size() >= 1, "Should return at least one member");

        // Verify structure of first member
        Member firstMember = members.get(0);
        assertNotNull(firstMember.getId(), "Member should have 'id' field");
        assertNotNull(firstMember.getName(), "Member should have 'name' field");
        assertNotNull(firstMember.getEmail(), "Member should have 'email' field");
        assertNotNull(firstMember.getPhoneNumber(), "Member should have 'phoneNumber' field");

        log.info("Successfully listed " + members.size() + " members");
    }

    @Test
    public void testLookupMemberById() {
        // First, create a test member
        Long memberId = createTestMember("Jane Smith", "jane.smith.lookup@example.com", "5559876543");

        // Test: GET /rest/members/{id}
        ResponseEntity<Member> response = restTemplate.getForEntity("/rest/members/" + memberId, Member.class);

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        Member member = response.getBody();

        // Verify member data
        assertEquals(memberId, member.getId(), "Should return correct ID");
        assertEquals("Jane Smith", member.getName(), "Should return correct name");
        assertEquals("jane.smith.lookup@example.com", member.getEmail(), "Should return correct email");
        assertEquals("5559876543", member.getPhoneNumber(), "Should return correct phone");

        log.info("Successfully looked up member by ID: " + memberId);
    }

    @Test
    public void testLookupMemberByIdNotFound() {
        // Test: GET /rest/members/{nonExistentId}
        ResponseEntity<String> response = restTemplate.getForEntity("/rest/members/999999", String.class);

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return 404 Not Found");

        log.info("Successfully verified 404 response for non-existent member ID");
    }

    @Test
    public void testListAllMembersIsOrderedByName() {
        // Create multiple members in non-alphabetical order
        createTestMember("Zoe Taylor", "zoe.rest@example.com", "5551111111");
        createTestMember("Alice Brown", "alice.rest@example.com", "5552222222");
        createTestMember("Mike Davis", "mike.rest@example.com", "5553333333");

        // Test: GET /rest/members
        ResponseEntity<List<Member>> response = restTemplate.exchange(
                "/rest/members",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Member>>() {}
        );

        List<Member> members = response.getBody();

        // Find our test members and verify they're in alphabetical order
        int aliceIndex = -1;
        int mikeIndex = -1;
        int zoeIndex = -1;

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            String email = member.getEmail();
            if (email.equals("alice.rest@example.com")) aliceIndex = i;
            if (email.equals("mike.rest@example.com")) mikeIndex = i;
            if (email.equals("zoe.rest@example.com")) zoeIndex = i;
        }

        assertTrue(aliceIndex >= 0, "Alice should be found");
        assertTrue(mikeIndex >= 0, "Mike should be found");
        assertTrue(zoeIndex >= 0, "Zoe should be found");
        assertTrue(aliceIndex < mikeIndex, "Alice should come before Mike");
        assertTrue(mikeIndex < zoeIndex, "Mike should come before Zoe");

        log.info("Successfully verified members are ordered alphabetically by name");
    }

    @Test
    public void testLookupMemberByInvalidId() {
        // Test: GET /rest/members/invalid (non-numeric ID)
        // Note: The path pattern is {id:[0-9][0-9]*} so this should return 404
        ResponseEntity<String> response = restTemplate.getForEntity("/rest/members/abc", String.class);

        // Should return 404 because the path doesn't match the pattern
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return 404 for invalid ID format");

        log.info("Successfully verified 404 response for invalid member ID format");
    }

    /**
     * Helper method to create a test member and return its ID
     */
    private Long createTestMember(String name, String email, String phoneNumber) {
        Map<String, String> memberData = Map.of(
                "name", name,
                "email", email,
                "phoneNumber", phoneNumber
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/rest/members", memberData, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Member creation should succeed");

        // Get the created member's ID by looking it up via the list endpoint
        ResponseEntity<List<Member>> getResponse = restTemplate.exchange(
                "/rest/members",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Member>>() {}
        );

        List<Member> members = getResponse.getBody();

        // Find the member we just created
        for (Member member : members) {
            if (member.getEmail().equals(email)) {
                return member.getId();
            }
        }

        throw new RuntimeException("Could not find created member with email: " + email);
    }
}
