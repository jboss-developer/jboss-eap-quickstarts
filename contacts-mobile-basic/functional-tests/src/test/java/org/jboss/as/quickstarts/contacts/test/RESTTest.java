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
package org.jboss.as.quickstarts.contacts.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Test for REST API of the application
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class RESTTest {

    private static final String NEW_MEMBER_FIRSTNAME = "John";
    private static final String NEW_MEMBER_LASTNAME = "Doe";
    private static final String NEW_MEMBER_EMAIL = "john.doe@redhat.com";
    private static final String NEW_MEMBER_PHONE = "1970-01-01T12:00:00.000Z";

    private static final String DEFAULT_MEMBER_FIRSTNAME = "John";
    private static final String DEFAULT_MEMBER_LASTNAME = "Smith";
    private static final int DEFAULT_MEMBER_ID = 10001;

    private static final String API_PATH = "rest/members/";

    private final DefaultHttpClient httpClient = new DefaultHttpClient();

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    /**
     * Creates deployment which is sent to the container upon test's start.
     *
     * @return war file which is deployed while testing, the whole application in our case
     */
    @Deployment(testable = false)
    public static WebArchive deployment() {
        return Deployments.contacts();
    }

    @Test
    @InSequence(1)
    public void testGetMember() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH + DEFAULT_MEMBER_ID));

        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject member = new JSONObject(responseBody);

        assertEquals(DEFAULT_MEMBER_ID, member.getInt("id"));
        assertEquals(DEFAULT_MEMBER_FIRSTNAME, member.getString("firstName"));
        assertEquals(DEFAULT_MEMBER_LASTNAME, member.getString("lastName"));
    }

    @Test
    @InSequence(2)
    public void testAddMember() throws Exception {
        HttpPost post = new HttpPost(contextPath.toString() + API_PATH);
        post.setHeader("Content-Type", "application/json");
        String newMemberJSON = new JSONStringer().object()
                .key("firstName").value(NEW_MEMBER_FIRSTNAME)
                .key("lastName").value(NEW_MEMBER_LASTNAME)
                .key("email").value(NEW_MEMBER_EMAIL)
                .key("phoneNumber").value(NEW_MEMBER_PHONE)
                .key("birthDate").value(NEW_MEMBER_PHONE)
                .endObject().toString();
        post.setEntity(new StringEntity(newMemberJSON));

        HttpResponse response = httpClient.execute(post);

        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    @InSequence(3)
    public void testGetAllMembers() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH));
        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONArray members = new JSONArray(responseBody);

        assertEquals(3, members.length());

        assertEquals(1, members.getJSONObject(0).getInt("id"));
        assertEquals(NEW_MEMBER_FIRSTNAME, members.getJSONObject(0).getString("firstName"));
        assertEquals(NEW_MEMBER_LASTNAME, members.getJSONObject(0).getString("lastName"));
        assertEquals(NEW_MEMBER_EMAIL, members.getJSONObject(0).getString("email"));
        assertEquals(NEW_MEMBER_PHONE, members.getJSONObject(0).getString("phoneNumber"));
    }
}
