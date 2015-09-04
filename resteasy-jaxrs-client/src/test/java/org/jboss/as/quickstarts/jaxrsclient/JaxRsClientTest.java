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
package org.jboss.as.quickstarts.jaxrsclient;

/**
 * This example demonstrates the use an external JAX-RS RestEasy client
 * which interacts with a JAX-RS Web service that uses CDI 1.0 and JAX-RS 
 * in Red Hat JBoss Enterprise Application Platform.  Specifically, 
 * this client "calls" the HelloWorld JAX-RS Web Service created in  
 * quickstart helloworld-rs.  Please refer to the helloworld-rs README.md 
 * for instructions on how to build and deploy helloworld-rs.
 */
import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit4 Test class which makes a request to the RESTful helloworld-rs web service.
 * 
 * @author bmincey (Blaine Mincey)
 * 
 */
public class JaxRsClientTest {
    /**
     * Request URLs pulled from system properties in pom.xml
     */
    private static String XML_URL;
    private static String JSON_URL;

    /**
     * Property names used to pull values from system properties in pom.xml
     */
    private static final String XML_PROPERTY = "xmlUrl";
    private static final String JSON_PROPERTY = "jsonUrl";

    /**
     * Responses of the RESTful web service
     */
    private static final String XML_RESPONSE = "<xml><result>Hello World!</result></xml>";
    private static final String JSON_RESPONSE = "{\"result\":\"Hello World!\"}";

    /**
     * Method executes BEFORE the test method. Values are read from system properties that can be modified in the pom.xml.
     */
    @BeforeClass
    public static void beforeClass() {
        JaxRsClientTest.XML_URL = System.getProperty(JaxRsClientTest.XML_PROPERTY);
        JaxRsClientTest.JSON_URL = System.getProperty(JaxRsClientTest.JSON_PROPERTY);
    }

    /**
     * Test method which executes the runRequest method that calls the RESTful helloworld-rs web service.
     */
    @Test
    public void test() {
        assertEquals("XML Response", JaxRsClientTest.XML_RESPONSE,
                this.runRequest(JaxRsClientTest.XML_URL, MediaType.APPLICATION_XML_TYPE));

        assertEquals("JSON Response", JaxRsClientTest.JSON_RESPONSE,
                this.runRequest(JaxRsClientTest.JSON_URL, MediaType.APPLICATION_JSON_TYPE));
    }

    /**
     * The purpose of this method is to run the external REST request.
     * 
     * @param url The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     */
    private String runRequest(String url, MediaType mediaType) {
        String result = null;

        System.out.println("===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());
        Response response = null;
        try {
            // Using the RESTEasy libraries, initiate a client request
            // set the url as a parameter
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(url);

            // Request has been made, now let's get the response
            response = target.request().get();

            // Check the HTTP status of the request
            // HTTP 200 indicates the request is OK
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
            }

            // We have a good response, let's now read it
            String value = response.readEntity(String.class);

            System.out.println("\n*** Response from Server ***\n");
            System.out.println(value);

            return value;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (response != null)
                response.close();
        }

        System.out.println("\n===============================================");

        return result;
    }

}
