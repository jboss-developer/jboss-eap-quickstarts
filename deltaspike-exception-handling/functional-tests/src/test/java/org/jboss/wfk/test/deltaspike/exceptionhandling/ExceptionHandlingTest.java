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
package org.jboss.wfk.test.deltaspike.exceptionhandling;

import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests DeltaSpike exception handling
 *
 * @author Ron Smeral
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ExceptionHandlingTest {

    private static final String MESSAGE_INVOCATIONS = "Forced %s. Service Invocation #%d";

    private static final String MY_EXCEPTION_COUNT = " MyException Count: %d";

    private static final String MESSAGE_REST = "Http Response Code 500: Forced MyException. Service Invocation #%d";

    private static final String REST_PATH = "rest/service/test";

    @FindByJQuery("input[value='Test MyException']")
    WebElement BTN_MYEXCEPTION;

    @FindByJQuery("input[value='Test MyOtherException']")
    WebElement BTN_MYOTHEREXCEPTION;

    @FindByJQuery("REST Invocation Test")
    WebElement LINK_REST;

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;

    private static final String TEST_DEPLOYMENT = "../target/jboss-deltaspike-exception-handling.war";

    @Deployment(testable = false)
    public static WebArchive deployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(TEST_DEPLOYMENT));
    }

    @Before
    public void beforeTest() {
        driver.navigate().to(contextPath);
    }

    @Test
    public void testExceptions() {
        guardHttp(BTN_MYEXCEPTION).click();
        assertTrue(driver.getPageSource().contains(String.format(MESSAGE_INVOCATIONS, "MyException", 1)));
        assertTrue(driver.getPageSource().contains(String.format(MY_EXCEPTION_COUNT, 1)));

        guardHttp(BTN_MYOTHEREXCEPTION).click();
        assertTrue(driver.getPageSource().contains(String.format(MESSAGE_INVOCATIONS, "MyOtherException", 2)));
        assertTrue(driver.getPageSource().contains(String.format(MY_EXCEPTION_COUNT, 1)));

        driver.navigate().to(contextPath + REST_PATH);
        assertTrue(driver.getPageSource().contains(String.format(MESSAGE_REST, 3)));
    }
}