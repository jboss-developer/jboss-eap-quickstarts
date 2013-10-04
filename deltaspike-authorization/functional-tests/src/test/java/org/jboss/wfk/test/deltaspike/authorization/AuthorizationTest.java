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
package org.jboss.wfk.test.deltaspike.authorization;

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
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests DeltaSpike authorization
 *
 * @author Ron Smeral
 */
@RunAsClient
@RunWith(Arquillian.class)
public class AuthorizationTest {

    private static final String USERNAME = "quickstartUser";
    private static final String PASSWORD = "quickstartPwd1!";
    private static final String LOGIN_PATH = "index.html";
    private static final String WELCOME_PATH = "welcome.jsf";
    private static final String WELCOME_HEADER = "Welcome to the restricted page!";
    private static final String EMPLOYEE_TEXT = "You executed a @EmployeeAllowed method";
    private static final String ADMIN_TEXT = "AccessDeniedException";

    @FindByJQuery("input[name='j_username']")
    WebElement LOGIN_USERNAME;

    @FindByJQuery("input[name='j_password']")
    WebElement LOGIN_PASSWORD;

    @FindByJQuery("input[value='Login']")
    WebElement LOGIN_BUTTON;

    @FindByJQuery("input[value='Logout']")
    WebElement LOGOUT_BUTTON;

    @FindByJQuery("input[value='Employees only Method']")
    WebElement EMPLOYEES_BUTTON;

    @FindByJQuery("input[value='Admins only Method']")
    WebElement ADMIN_BUTTON;

    private static final String TEST_DEPLOYMENT = "../target/jboss-deltaspike-authorization.war";

    @Deployment(testable = false)
    public static WebArchive deployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(TEST_DEPLOYMENT));
    }

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;

    @Before
    public void beforeTest() {
        login();
    }

    @After
    public void afterTest() {
        logout();
    }

    @Test
    public void loginTest() {
        assertTrue(driver.getPageSource().contains(WELCOME_HEADER));
    }

    @Test
    public void employeeTest() {
        guardHttp(EMPLOYEES_BUTTON).click();
        assertTrue(driver.getPageSource().contains(EMPLOYEE_TEXT));
    }

    @Test
    public void adminTest() {
        guardHttp(ADMIN_BUTTON).click();
        assertTrue(driver.getPageSource().contains(ADMIN_TEXT));
    }

    private void login() {
        driver.navigate().to(contextPath + LOGIN_PATH);
        LOGIN_USERNAME.clear();
        LOGIN_USERNAME.sendKeys(USERNAME);

        LOGIN_PASSWORD.clear();
        LOGIN_PASSWORD.sendKeys(PASSWORD);

        guardHttp(LOGIN_BUTTON).click();
    }

    private void logout() {
        driver.navigate().to(contextPath + WELCOME_PATH);
        guardHttp(LOGOUT_BUTTON).click();
    }
}