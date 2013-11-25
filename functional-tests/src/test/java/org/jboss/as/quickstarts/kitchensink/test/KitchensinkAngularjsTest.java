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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;
import java.util.List;

import static junit.framework.Assert.*;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * Kitchensink Angular.js quickstart functional test
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class KitchensinkAngularjsTest {

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
     * Injects JavascriptExecutor for executing javascript on opened page
     */
    @ArquillianResource
    JavascriptExecutor javascript;

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
    @FindBy(id = "register")
    WebElement registerButton;

    /**
     * Locator for registration success message
     */
    @FindByJQuery("ul.success li:first")
    WebElement registeredMessageSuccess;

    /**
     * Locator for rows of the members table
     */
    @FindByJQuery("table tr.ng-scope")
    List<WebElement> tableMembersRows;

    /**
     * Locator for columns of the first row of the members table
     */
    @FindByJQuery("table tr.ng-scope:first td")
    List<WebElement> tableMembersRowColumns;

    /**
     * Locator for name field validation message
     */
    @FindByJQuery("span[ng-show='errors.name']")
    WebElement nameErrorMessage;

    /**
     * Locator for registration form
     */
    @FindBy(id = "reg")
    WebElement registrationForm;

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


    /**
     * This method tests there is no new member in the registration table when
     * all three input fields are empty.
     */
    @Test
    @InSequence(1)
    public void testEmptyRegistration() {
        browser.get(contextPath.toString());
        registerButton.click();
        assertTrue(new WebElementConditionFactory(registeredMessageSuccess).not().isPresent().apply(browser));
    }

    /**
     * This method tests registration of the new member with the name of bad
     * formats.
     */
    @Test
    @InSequence(2)
    public void testRegistrationWithBadNameFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_BAD, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        registerButton.click();
        waitModel().until("Name validation message should be present").element(nameErrorMessage).is().visible();
        assertTrue(new WebElementConditionFactory(registeredMessageSuccess).not().isPresent().apply(browser));
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_TOO_LONG, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        registerButton.click();
        waitModel().until("Name validation message should be present").element(nameErrorMessage).is().visible();
        assertTrue(new WebElementConditionFactory(registeredMessageSuccess).not().isPresent().apply(browser));
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    /**
     * This method tests registration of the new member with the email of bad
     * format.
     */
    @Test
    @InSequence(3)
    public void testRegistrationWithBadEmailFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_1, PHONE_FORMAT_OK);
        assertFalse(isValid(emailField));

//        ARQGRA-331 - Graphene guards do not work with AngularJS
//        guardNoRequest(registerButton).click();
//        assertTrue(element(registeredMessageSuccess).not().isPresent().apply(browser));
//        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_2, PHONE_FORMAT_OK);
        assertFalse(isValid(emailField));
    }

    /**
     * This method tests registration of the new member with the phone of bad
     * format
     */
    @Test
    @InSequence(4)
    public void testRegistrationWithBadPhoneFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_ILLEGAL_CHARS);
        assertFalse(isValid(phoneField));

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_SHORT);
        assertFalse(isValid(phoneField));

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_LONG);
        assertFalse(isValid(phoneField));
    }

    /**
     * This method tests regular registration process
     */
    @Test
    @InSequence(5)
    public void testRegularRegistration() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        assertTrue(isValid(registrationForm));
        registerButton.click();

        waitModel().until().element(registeredMessageSuccess).is().visible();

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

    /**
     * Helper method for executing checkValidity() javascript method from HTML5 form validation API
     *
     * @param element Element to be checked
     * @return Element validity
     */
    private boolean isValid(WebElement element) {
        return (Boolean) javascript.executeScript("return arguments[0].checkValidity()", element);
    }

}
