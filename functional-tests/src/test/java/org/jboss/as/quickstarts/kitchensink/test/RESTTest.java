/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.kitchensink.test;

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

    private static final String NEW_MEMBER_NAME = "John Doe";
    private static final String NEW_MEMBER_EMAIL = "john.doe@redhat.com";
    private static final String NEW_MEMBER_PHONE = "1234567890";
    private static final String DEFAULT_MEMBER_NAME = "John Smith";

    private static final String API_PATH = "rest/members";

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
        return Deployments.kitchensink();
    }

    @Test
    @InSequence(1)
    public void testGetMember() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH + "/0"));

        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject member = new JSONObject(responseBody);

        assertEquals(0, member.getInt("id"));
        assertEquals(DEFAULT_MEMBER_NAME, member.getString("name"));
    }

    @Test
    @InSequence(2)
    public void testAddMember() throws Exception {
        HttpPost post = new HttpPost(contextPath.toString() + API_PATH);
        post.setHeader("Content-Type", "application/json");
        String newMemberJSON = new JSONStringer().object()
                .key("name").value(NEW_MEMBER_NAME)
                .key("email").value(NEW_MEMBER_EMAIL)
                .key("phoneNumber").value(NEW_MEMBER_PHONE)
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

        assertEquals(2, members.length());

        assertEquals(1, members.getJSONObject(0).getInt("id"));
        assertEquals(NEW_MEMBER_NAME, members.getJSONObject(0).getString("name"));
        assertEquals(NEW_MEMBER_EMAIL, members.getJSONObject(0).getString("email"));
        assertEquals(NEW_MEMBER_PHONE, members.getJSONObject(0).getString("phoneNumber"));

        assertEquals(0, members.getJSONObject(1).getInt("id"));
        assertEquals(DEFAULT_MEMBER_NAME, members.getJSONObject(1).getString("name"));
    }
}
