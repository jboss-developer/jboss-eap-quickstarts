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
 * Integration tests for duplicate email handling.
 * Tests that the system properly prevents duplicate email registrations.
 */
public class DuplicateEmailIT {

    private static final Logger log = Logger.getLogger(DuplicateEmailIT.class.getName());
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
    public void testDuplicateEmailRejected() throws Exception {
        String duplicateEmail = "duplicate.test@example.com";

        // First registration - should succeed
        JsonObject json1 = Json.createObjectBuilder()
                .add("name", "First User")
                .add("email", duplicateEmail)
                .add("phoneNumber", "1234567890")
                .build();

        HttpRequest request1 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json1.toString()))
                .build();

        HttpResponse<String> response1 = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals("First registration should succeed", 200, response1.statusCode());
        log.info("First registration succeeded for: " + duplicateEmail);

        // Second registration with same email - should fail
        JsonObject json2 = Json.createObjectBuilder()
                .add("name", "Second User")
                .add("email", duplicateEmail)
                .add("phoneNumber", "0987654321")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json2.toString()))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        // Verify duplicate is rejected with 409 Conflict
        Assertions.assertEquals("Duplicate email should return 409 Conflict", 409, response2.statusCode());
        Assertions.assertNotNull("Response body should not be null", response2.body());

        // Parse error response
        JsonReader jsonReader = Json.createReader(new StringReader(response2.body()));
        JsonObject errorResponse = jsonReader.readObject();

        // Verify error message mentions email
        Assertions.assertTrue("Error response should contain 'email' field", errorResponse.containsKey("email"));
        String emailError = errorResponse.getString("email");
        Assertions.assertTrue("Error message should mention 'taken' or similar",
            emailError.toLowerCase().contains("taken") || emailError.toLowerCase().contains("exists"));

        log.info("Successfully rejected duplicate email with 409 Conflict: " + duplicateEmail);
    }

    @Test
    public void testDuplicateEmailWithDifferentCase() throws Exception {
        String baseEmail = "case.sensitive@example.com";

        // First registration with lowercase
        JsonObject json1 = Json.createObjectBuilder()
                .add("name", "Lowercase User")
                .add("email", baseEmail)
                .add("phoneNumber", "1112223333")
                .build();

        HttpRequest request1 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json1.toString()))
                .build();

        HttpResponse<String> response1 = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals("First registration should succeed", 200, response1.statusCode());

        // Second registration with different case
        String upperCaseEmail = baseEmail.toUpperCase();
        JsonObject json2 = Json.createObjectBuilder()
                .add("name", "Uppercase User")
                .add("email", upperCaseEmail)
                .add("phoneNumber", "4445556666")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json2.toString()))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        // This test documents the current behavior - whether case-sensitive or not
        // If the database constraint is case-insensitive, this should return 409
        // If case-sensitive, this should return 200
        log.info("Registration with different case returned status: " + response2.statusCode());

        // For now, we just document the behavior without asserting
        // During migration, you may want to enforce case-insensitive email uniqueness
        if (response2.statusCode() == 409) {
            log.info("System enforces case-insensitive email uniqueness");
        } else if (response2.statusCode() == 200) {
            log.info("System allows different cases as different emails");
        }
    }

    @Test
    public void testMultipleUsersWithUniqueEmails() throws Exception {
        // Test that multiple users can register with different emails
        String email1 = "unique1@example.com";
        String email2 = "unique2@example.com";
        String email3 = "unique3@example.com";

        // Register first user
        JsonObject json1 = Json.createObjectBuilder()
                .add("name", "User One")
                .add("email", email1)
                .add("phoneNumber", "1231231234")
                .build();
        HttpRequest request1 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json1.toString()))
                .build();
        HttpResponse<String> response1 = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals("First user should register successfully", 200, response1.statusCode());

        // Register second user
        JsonObject json2 = Json.createObjectBuilder()
                .add("name", "User Two")
                .add("email", email2)
                .add("phoneNumber", "4564564567")
                .build();
        HttpRequest request2 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json2.toString()))
                .build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals("Second user should register successfully", 200, response2.statusCode());

        // Register third user
        JsonObject json3 = Json.createObjectBuilder()
                .add("name", "User Three")
                .add("email", email3)
                .add("phoneNumber", "7897897890")
                .build();
        HttpRequest request3 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json3.toString()))
                .build();
        HttpResponse<String> response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals("Third user should register successfully", 200, response3.statusCode());

        log.info("Successfully registered 3 users with unique emails");
    }

    @Test
    public void testDuplicateEmailErrorMessageFormat() throws Exception {
        String duplicateEmail = "format.test@example.com";

        // First registration
        JsonObject json1 = Json.createObjectBuilder()
                .add("name", "Original User")
                .add("email", duplicateEmail)
                .add("phoneNumber", "5556667777")
                .build();

        HttpRequest request1 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json1.toString()))
                .build();

        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        // Second registration with duplicate email
        JsonObject json2 = Json.createObjectBuilder()
                .add("name", "Duplicate User")
                .add("email", duplicateEmail)
                .add("phoneNumber", "8889990000")
                .build();

        HttpRequest request2 = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json2.toString()))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        // Verify error response format
        Assertions.assertEquals("Should return 409 Conflict", 409, response2.statusCode());

        JsonReader jsonReader = Json.createReader(new StringReader(response2.body()));
        JsonObject errorResponse = jsonReader.readObject();

        // Verify it's a proper JSON object with email field
        Assertions.assertTrue("Should be a JSON object with 'email' field", errorResponse.containsKey("email"));
        Assertions.assertTrue("Email field should be a string", errorResponse.get("email").getValueType() == jakarta.json.JsonValue.ValueType.STRING);
        Assertions.assertFalse("Error message should not be empty", errorResponse.getString("email").isEmpty());

        log.info("Duplicate email error format verified: " + errorResponse.toString());
    }
}
