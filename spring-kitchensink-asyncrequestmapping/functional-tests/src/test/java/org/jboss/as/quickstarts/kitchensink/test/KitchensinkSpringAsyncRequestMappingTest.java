/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Kitchensink Spring AsyncRequestMapping quickstart functional test
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class KitchensinkSpringAsyncRequestMappingTest {

    /**
     * Injects browser to our test.
     */
    @Drone
    WebDriver browser;

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

    /**
     * Locator for name field
     */
    @FindBy(id = "name")
    WebElement nameField;

    /**
     * Locator for email field
     */
    @FindBy(id = "email")
    WebElement emailField;

    /**
     * Locator for phone number field
     */
    @FindBy(id = "phoneNumber")
    WebElement phoneField;

    /**
     * Locator for registration button
     */
    @FindByJQuery("input.register")
    WebElement registerButton;

    /**
     * Locator for rows of the members table
     */
    @FindByJQuery("table.simpletablestyle:first tbody tr")
    List<WebElement> tableMembersRows;

    /**
     * Locator for columns of the first row of the members table
     */
    @FindByJQuery("table.simpletablestyle:first tbody tr:first td")
    List<WebElement> tableMembersRowColumns;

    /**
     * Locator for name field validation message
     */
    @FindBy(id = "name.errors")
    WebElement nameErrorMessage;

    /**
     * Locator for email field validation message
     */
    @FindBy(id = "email.errors")
    WebElement emailErrorMessage;

    /**
     * Locator for phone number field validation message
     */
    @FindBy(id = "phoneNumber.errors")
    WebElement phoneErrorMessage;

    /**
     * Name of the member to register in the right format.
     */
    private static final String NAME_FORMAT_OK = "John Doe";

    /**
     * Name of the member to register in the bad format.
     */
    private static final String NAME_FORMAT_BAD = "John1";

    /**
     * Name of the member to register which is too long (1-25)
     */
    private static final String NAME_FORMAT_TOO_LONG = "John Doe John Doe John Doe";

    /**
     * E-mail of the member to register in the right format.
     */
    private static final String EMAIL_FORMAT_OK = "john@doe.com";

    /**
     * E-mail of the member to register in the bad format - #1.
     */
    private static final String EMAIL_FORMAT_BAD_1 = "joe";

    /**
     * E-mail of the member to register in the bad format - #2.
     */
    private static final String EMAIL_FORMAT_BAD_2 = "john@doe.com ";

    /**
     * Phone number of the member to register in the right format.
     */
    private static final String PHONE_FORMAT_OK = "0123456789";

    /**
     * Phone number of the member to register in the bad format - illegal
     * characters.
     */
    private static final String PHONE_FORMAT_BAD_ILLEGAL_CHARS = "as/df.123@";

    /**
     * Phone number of the member to register in the bad format - too long.
     */
    private static final String PHONE_FORMAT_BAD_TOO_LONG = "12345678901234567890";

    /**
     * Phone nuber of the member to register in the bad format - too short
     */
    private static final String PHONE_FORMAT_BAD_TOO_SHORT = "123456789";


    @Test
    @InSequence(1)
    public void testEmptyRegistration() {
        browser.get(contextPath.toString());
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertTrue("Email validation message should be present", emailErrorMessage.isDisplayed());
        assertTrue("PhoneNumber validation message should be present", phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(2)
    public void testRegistrationWithBadNameFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_BAD, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_TOO_LONG, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(3)
    public void testRegistrationWithBadEmailFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_1, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue(emailErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_2, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue(emailErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(4)
    public void testRegistrationWithBadPhoneFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_ILLEGAL_CHARS);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_SHORT);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_LONG);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(5)
    public void testRegularRegistration() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();

        assertEquals(2, tableMembersRows.size());
        assertEquals(5, tableMembersRowColumns.size());

        assertTrue((tableMembersRowColumns.get(1)).getText().equals(NAME_FORMAT_OK));
        assertTrue((tableMembersRowColumns.get(2)).getText().equals(EMAIL_FORMAT_OK));
        assertTrue((tableMembersRowColumns.get(3)).getText().equals(PHONE_FORMAT_OK));
    }

    /**
     * This helper method sets values into the according input fields.
     *
     * @param name  name to set into the name input field
     * @param email email to set into the email input field
     * @param phone phone to set into the phone input field
     */
    private void setInputFields(String name, String email, String phone) {
        nameField.clear();
        nameField.sendKeys(name);
        emailField.clear();
        emailField.sendKeys(email);
        phoneField.clear();
        phoneField.sendKeys(phone);
    }

}
