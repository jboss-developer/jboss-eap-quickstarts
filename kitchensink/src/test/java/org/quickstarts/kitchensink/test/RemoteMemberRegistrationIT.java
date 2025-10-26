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

import org.quickstarts.kitchensink.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Spring Boot integration test for member registration.
 * Uses @SpringBootTest to start the full application context and TestRestTemplate for HTTP calls.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RemoteMemberRegistrationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRegister() {
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/rest/members",
            newMember,
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Registration should succeed");
        // Response body is null for successful registration with no content
    }
}
