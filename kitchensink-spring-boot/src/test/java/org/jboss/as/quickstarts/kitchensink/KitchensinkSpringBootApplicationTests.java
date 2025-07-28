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
package org.jboss.as.quickstarts.kitchensink;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.repository.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the kitchensink Spring Boot application.
 * Tests all layers of the application: REST endpoints, service methods, and repository operations.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class KitchensinkSpringBootApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    // ================ REST API Tests ================

    @Test
    public void testListAllMembersEndpoint() {
        ResponseEntity<List<Member>> response = restTemplate.exchange(
                "/api/members",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Member>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Member> members = response.getBody();
        assertThat(members).isNotNull();
        assertThat(members.size()).isGreaterThanOrEqualTo(4); // At least the 4 members from import.sql
        
        // Verify members are ordered by name
        for (int i = 0; i < members.size() - 1; i++) {
            assertThat(members.get(i).getName().compareTo(members.get(i + 1).getName())).isLessThanOrEqualTo(0);
        }
    }

    @Test
    public void testGetMemberByIdEndpoint() {
        // Test successful retrieval
        ResponseEntity<Member> response = restTemplate.getForEntity("/api/members/0", Member.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Member member = response.getBody();
        assertThat(member).isNotNull();
        assertThat(member.getId()).isEqualTo(0L);
        assertThat(member.getName()).isEqualTo("John Smith");
        
        // Test not found
        ResponseEntity<Map<String, String>> errorResponse = restTemplate.exchange(
                "/api/members/999",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, String>>() {});
        
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateMemberEndpoint() {
        // Create a valid member
        Member newMember = new Member();
        newMember.setName("Test User");
        newMember.setEmail("test.user@example.com");
        newMember.setPhoneNumber("2125559999");

        ResponseEntity<Member> response = restTemplate.postForEntity("/api/members", newMember, Member.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Member createdMember = response.getBody();
        assertThat(createdMember).isNotNull();
        assertThat(createdMember.getId()).isNotNull();
        assertThat(createdMember.getName()).isEqualTo("Test User");
        assertThat(createdMember.getEmail()).isEqualTo("test.user@example.com");
        assertThat(createdMember.getPhoneNumber()).isEqualTo("2125559999");
    }

    @Test
    public void testCreateMemberWithValidationErrors() {
        // Create an invalid member (missing required fields)
        Member invalidMember = new Member();
        invalidMember.setName(""); // Invalid: empty name
        invalidMember.setEmail("not-an-email"); // Invalid: not a valid email
        invalidMember.setPhoneNumber("123"); // Invalid: too short

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/members",
                HttpMethod.POST,
                new HttpEntity<>(invalidMember),
                new ParameterizedTypeReference<Map<String, String>>() {});
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, String> errors = response.getBody();
        assertThat(errors).isNotNull();
        assertThat(errors.size()).isGreaterThanOrEqualTo(3); // At least 3 validation errors
        assertThat(errors).containsKey("name");
        assertThat(errors).containsKey("email");
        assertThat(errors).containsKey("phoneNumber");
    }

    @Test
    public void testCreateMemberWithDuplicateEmail() {
        // Create a member with an email that already exists in import.sql
        Member duplicateMember = new Member();
        duplicateMember.setName("Another John");
        duplicateMember.setEmail("john.smith@mailinator.com"); // This email already exists
        duplicateMember.setPhoneNumber("2125551111");

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/members",
                HttpMethod.POST,
                new HttpEntity<>(duplicateMember),
                new ParameterizedTypeReference<Map<String, String>>() {});
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        Map<String, String> errors = response.getBody();
        assertThat(errors).isNotNull();
        assertThat(errors).containsKey("email");
    }

    // ================ Service Layer Tests ================

    @Test
    public void testMemberServiceRegistration() throws Exception {
        // Create a new member
        Member newMember = new Member();
        newMember.setName("Service Test");
        newMember.setEmail("service.test@example.com");
        newMember.setPhoneNumber("2125558888");

        // Register the member
        Member registeredMember = memberService.register(newMember);
        
        // Verify the member was registered
        assertNotNull(registeredMember.getId());
        assertEquals("Service Test", registeredMember.getName());
        
        // Verify the member can be found in the database
        Member foundMember = memberService.findById(registeredMember.getId());
        assertNotNull(foundMember);
        assertEquals("service.test@example.com", foundMember.getEmail());
    }

    @Test
    public void testMemberServiceFindByEmail() {
        // Find an existing member by email
        Member member = memberService.findByEmail("john.smith@mailinator.com");
        
        assertNotNull(member);
        assertEquals("John Smith", member.getName());
        assertEquals("2125551212", member.getPhoneNumber());
        
        // Try to find a non-existent email
        Member nonExistentMember = memberService.findByEmail("nonexistent@example.com");
        assertNull(nonExistentMember);
    }

    @Test
    public void testMemberServiceFindAllOrderedByName() {
        List<Member> members = memberService.findAllOrderedByName();
        
        assertNotNull(members);
        assertTrue(members.size() >= 4); // At least the 4 members from import.sql
        
        // Verify members are ordered by name
        for (int i = 0; i < members.size() - 1; i++) {
            assertTrue(members.get(i).getName().compareTo(members.get(i + 1).getName()) <= 0);
        }
    }

    // ================ Repository Layer Tests ================

    @Test
    public void testMemberRepositoryOperations() {
        // Create and save a new member
        Member newMember = new Member();
        newMember.setName("Repo Test");
        newMember.setEmail("repo.test@example.com");
        newMember.setPhoneNumber("2125557777");
        
        Member savedMember = memberRepository.save(newMember);
        assertNotNull(savedMember.getId());
        
        // Find by ID
        Member foundById = memberRepository.findById(savedMember.getId()).orElse(null);
        assertNotNull(foundById);
        assertEquals("Repo Test", foundById.getName());
        
        // Find by email
        Member foundByEmail = memberRepository.findByEmail("repo.test@example.com");
        assertNotNull(foundByEmail);
        assertEquals(savedMember.getId(), foundByEmail.getId());
        
        // Find all ordered by name
        List<Member> allMembers = memberRepository.findAllByOrderByNameAsc();
        assertNotNull(allMembers);
        assertTrue(allMembers.size() >= 5); // At least 4 from import.sql + 1 we just added
        
        // Verify order
        for (int i = 0; i < allMembers.size() - 1; i++) {
            assertTrue(allMembers.get(i).getName().compareTo(allMembers.get(i + 1).getName()) <= 0);
        }
    }

    // ================ Error Handling Tests ================

    @Test
    @DirtiesContext // Reset the context after this test to avoid side effects
    public void testErrorHandlingForConstraintViolations() {
        // Test with a name containing numbers (violates pattern constraint)
        Member invalidMember = new Member();
        invalidMember.setName("User123"); // Contains numbers, violates pattern
        invalidMember.setEmail("valid.email@example.com");
        invalidMember.setPhoneNumber("2125556666");

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/members",
                HttpMethod.POST,
                new HttpEntity<>(invalidMember),
                new ParameterizedTypeReference<Map<String, String>>() {});
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, String> errors = response.getBody();
        assertThat(errors).isNotNull();
        assertThat(errors).containsKey("name");
        assertThat(errors.get("name")).contains("Must not contain numbers");
    }

    @Test
    public void testErrorHandlingForInvalidPhoneNumber() {
        // Test with an invalid phone number (too long)
        Member invalidMember = new Member();
        invalidMember.setName("Valid Name");
        invalidMember.setEmail("another.valid@example.com");
        invalidMember.setPhoneNumber("21255566661234"); // Too long

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                "/api/members",
                HttpMethod.POST,
                new HttpEntity<>(invalidMember),
                new ParameterizedTypeReference<Map<String, String>>() {});
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, String> errors = response.getBody();
        assertThat(errors).isNotNull();
        assertThat(errors).containsKey("phoneNumber");
    }
}
