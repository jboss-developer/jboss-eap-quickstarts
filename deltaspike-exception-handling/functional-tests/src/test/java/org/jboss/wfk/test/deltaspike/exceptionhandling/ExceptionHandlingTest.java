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