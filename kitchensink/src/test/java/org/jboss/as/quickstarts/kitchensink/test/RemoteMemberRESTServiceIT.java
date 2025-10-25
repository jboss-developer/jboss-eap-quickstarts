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
package org.jboss.as.quickstarts.kitchensink.test;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for REST GET endpoints.
 * Tests member listing and lookup operations via HTTP.
 */
public class RemoteMemberRESTServiceIT {

    private static final Logger log = Logger.getLogger(RemoteMemberRESTServiceIT.class.getName());
    private HttpClient httpClient;

    @BeforeEach
    public void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    protected URI getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/kitchensink";
        }
        try {
            return new URI(host + "/rest/members");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Test
    public void testListAllMembers() throws Exception {
        // First, create a test member to ensure there's at least one in the database
        createTestMember("John Doe", "john.doe.list@example.com", "5551234567");

        // Test: GET /rest/members
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify response
        Assertions.assertEquals("Should return 200 OK", 200, response.statusCode());
        Assertions.assertNotNull("Response body should not be null", response.body());

        // Parse JSON array
        JsonReader jsonReader = Json.createReader(new StringReader(response.body()));
        JsonArray members = jsonReader.readArray();

        Assertions.assertTrue("Should return at least one member", members.size() >= 1);

        // Verify JSON structure of first member
        JsonObject firstMember = members.getJsonObject(0);
        Assertions.assertTrue("Member should have 'id' field", firstMember.containsKey("id"));
        Assertions.assertTrue("Member should have 'name' field", firstMember.containsKey("name"));
        Assertions.assertTrue("Member should have 'email' field", firstMember.containsKey("email"));
        Assertions.assertTrue("Member should have 'phoneNumber' field", firstMember.containsKey("phoneNumber"));

        log.info("Successfully listed " + members.size() + " members");
    }

    @Test
    public void testLookupMemberById() throws Exception {
        // First, create a test member
        Long memberId = createTestMember("Jane Smith", "jane.smith.lookup@example.com", "5559876543");

        // Test: GET /rest/members/{id}
        URI memberUri = new URI(getHTTPEndpoint().toString() + "/" + memberId);
        HttpRequest request = HttpRequest.newBuilder(memberUri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify response
        Assertions.assertEquals("Should return 200 OK", 200, response.statusCode());
        Assertions.assertNotNull("Response body should not be null", response.body());

        // Parse JSON object
        JsonReader jsonReader = Json.createReader(new StringReader(response.body()));
        JsonObject member = jsonReader.readObject();

        // Verify member data
        Assertions.assertEquals("Should return correct ID", memberId.longValue(), member.getJsonNumber("id").longValue());
        Assertions.assertEquals("Should return correct name", "Jane Smith", member.getString("name"));
        Assertions.assertEquals("Should return correct email", "jane.smith.lookup@example.com", member.getString("email"));
        Assertions.assertEquals("Should return correct phone", "5559876543", member.getString("phoneNumber"));

        log.info("Successfully looked up member by ID: " + memberId);
    }

    @Test
    public void testLookupMemberByIdNotFound() throws Exception {
        // Test: GET /rest/members/{nonExistentId}
        URI memberUri = new URI(getHTTPEndpoint().toString() + "/999999");
        HttpRequest request = HttpRequest.newBuilder(memberUri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify response
        Assertions.assertEquals("Should return 404 Not Found", 404, response.statusCode());

        log.info("Successfully verified 404 response for non-existent member ID");
    }

    @Test
    public void testListAllMembersIsOrderedByName() throws Exception {
        // Create multiple members in non-alphabetical order
        createTestMember("Zoe Taylor", "zoe.rest@example.com", "5551111111");
        createTestMember("Alice Brown", "alice.rest@example.com", "5552222222");
        createTestMember("Mike Davis", "mike.rest@example.com", "5553333333");

        // Test: GET /rest/members
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON array
        JsonReader jsonReader = Json.createReader(new StringReader(response.body()));
        JsonArray members = jsonReader.readArray();

        // Find our test members and verify they're in alphabetical order
        int aliceIndex = -1;
        int mikeIndex = -1;
        int zoeIndex = -1;

        for (int i = 0; i < members.size(); i++) {
            JsonObject member = members.getJsonObject(i);
            String email = member.getString("email");
            if (email.equals("alice.rest@example.com")) aliceIndex = i;
            if (email.equals("mike.rest@example.com")) mikeIndex = i;
            if (email.equals("zoe.rest@example.com")) zoeIndex = i;
        }

        Assertions.assertTrue("Alice should be found", aliceIndex >= 0);
        Assertions.assertTrue("Mike should be found", mikeIndex >= 0);
        Assertions.assertTrue("Zoe should be found", zoeIndex >= 0);
        Assertions.assertTrue("Alice should come before Mike", aliceIndex < mikeIndex);
        Assertions.assertTrue("Mike should come before Zoe", mikeIndex < zoeIndex);

        log.info("Successfully verified members are ordered alphabetically by name");
    }

    @Test
    public void testLookupMemberByInvalidId() throws Exception {
        // Test: GET /rest/members/invalid (non-numeric ID)
        // Note: The path pattern is {id:[0-9][0-9]*} so this should return 404
        URI memberUri = new URI(getHTTPEndpoint().toString() + "/abc");
        HttpRequest request = HttpRequest.newBuilder(memberUri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Should return 404 because the path doesn't match the pattern
        Assertions.assertEquals("Should return 404 for invalid ID format", 404, response.statusCode());

        log.info("Successfully verified 404 response for invalid member ID format");
    }

    /**
     * Helper method to create a test member and return its ID
     */
    private Long createTestMember(String name, String email, String phoneNumber) throws Exception {
        JsonObject json = Json.createObjectBuilder()
                .add("name", name)
                .add("email", email)
                .add("phoneNumber", phoneNumber)
                .build();

        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals("Member creation should succeed", 200, response.statusCode());

        // Get the created member's ID by looking it up via the list endpoint
        HttpRequest getRequest = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        JsonReader jsonReader = Json.createReader(new StringReader(getResponse.body()));
        JsonArray members = jsonReader.readArray();

        // Find the member we just created
        for (int i = 0; i < members.size(); i++) {
            JsonObject member = members.getJsonObject(i);
            if (member.getString("email").equals(email)) {
                return member.getJsonNumber("id").longValue();
            }
        }

        throw new RuntimeException("Could not find created member with email: " + email);
    }
}
