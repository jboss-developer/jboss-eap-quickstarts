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
package org.jboss.as.quickstarts.servlet_security_genericheader_auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.web.tomcat.security.GenericHeaderAuthenticator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Simple set of tests to demonstrate the GenericHeaderAuthenticator functionality.
 */
@RunWith(Arquillian.class)
public class TestGenericHeaderAuthenticator {
    /**
     * The location of the WebApp source folder so we know where to find the web.xml when deploying using Arquillian.
     */
    private static final String WEBAPP_SRC = "src/main/webapp";
    /**
     * The location of the test resources folder so we know where to find the properties file.
     */
    private static final String TEST_RESOURCES_SRC = "src/test/resources";

    /**
     * The name of the WAR Archive that will be used by Arquillian to deploy the application.
     */
    private static final String APP_NAME = "jboss-servlet-security-genericheader-auth";

    /**
     * Properties file location
     */
    private static final String PROPERTIES_FILE = "/test.properties";
    
    /**
     * The name for the Server URL Property.
     */
    private static final String SERVER_URL_PROPERTY = "serverUrl";

    /**
     * The Default Server URL if one isn't specified as a System Property
     */
    private static final String DEFAULT_SERVER_URL = "http://localhost:8080/";

    private URL deploymentUrl;

    @Deployment
    public static Archive<?> getDeployment() {

        Archive<?> archive = ShrinkWrap
                .create(WebArchive.class, APP_NAME + ".war")
                .addPackages(true, SecuredServlet.class.getPackage())
                .addPackages(true, GenericHeaderAuthenticator.class.getPackage())
                .addAsResource(new File(WEBAPP_SRC, "/index.html"))
                .addAsResource(new File(WEBAPP_SRC, "/login.html"))
                .addAsResource(new File(WEBAPP_SRC, "/error.html"))
                .addAsResource(new File(TEST_RESOURCES_SRC, PROPERTIES_FILE))
                .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/web.xml"))
                .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/jboss-web.xml"));
        return archive;
    }
    

    @Before
    public void beforeTest() throws MalformedURLException {
        Properties props = loadProperties();
        String deploymentUrl = props.getProperty(SERVER_URL_PROPERTY);
        
        // Check that the server URL property was set. If it wasn't then use the default.
        if (deploymentUrl == null || deploymentUrl.isEmpty()) {
            deploymentUrl = DEFAULT_SERVER_URL;
        }
        
        // Ensure that the URL ends with a forward slash
        if (!deploymentUrl.endsWith("/")) {
            deploymentUrl += "/";
        }
        
        // Ensure the App Name is specified in the URL
        if (!deploymentUrl.matches(".*" + APP_NAME + ".*"))
        {
            deploymentUrl += APP_NAME + "/SecuredServlet?unitTest=true";
        }
        
        // Set the deployment url
        this.deploymentUrl = new URL(deploymentUrl);
    }

    @Test
    public void testAuthentication() {
        try {
            URLConnection urlConn = deploymentUrl.openConnection();
            urlConn.setRequestProperty("sm_ssoid", "quickstartUser");
            urlConn.setRequestProperty("Cookie", "SMSESSION=quickstartUser");
            
            InputStream inputStream = urlConn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            assertEquals("Servlet should respond with \"AUTHENTICATED\" message", "AUTHENTICATED", line);
        } catch (IOException e) {
            fail("Authentication test connection failed!");
        }
    }

    private Properties loadProperties () {
        try {
            Properties props = new Properties();
            InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE);
            try {
                props.load(is);
            } finally {
                is.close();
            }
            return props;
        } catch (IOException e) {
            fail("Failed to load test.properties from classpath!");
            return null;
        }
    }
}
