/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.kitchensink.test.webdriver;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.openqa.selenium.WebElement;

/**
 * This class represent actual testing cases.
 */
public class KitchensinkTestCase extends KitchensinkBase {

    // Locators

    /**
     * Locator for registration form
     */
    @FindBy(jquery = "#reg")
    private WebElement REGISTRATION_FORM;

    /**
     * Locator for the input field for entering of a name of the member we want to register.
     */
    @FindBy(jquery = "input[id='reg:name']")
    private WebElement NAME_FIELD;

    /**
     * Locator for the input field for entering of an e-mail of the new member we want to register.
     */
    @FindBy(jquery = "input[id='reg:email']")
    private WebElement EMAIL_FIELD;

    /**
     * Locator for the input field for entering of a telephone number of the new member we want to register.
     */
    @FindBy(jquery = "input[id='reg:phoneNumber']")
    private WebElement PHONE_FIELD;

    /**
     * This class variable holds locator for the registration button we click on while registering a new member.
     */
    @FindBy(jquery = "input[id='reg:register']")
    private WebElement REGISTER_BUTTON;

    /**
     * This holds the locator for the error message about exceeded length of a name or allowed characters in a name.
     */
    @FindBy(jquery = "html body div#container div#content form#reg table tbody tr:eq(0) td span.invalid")
    private WebElement NAME_INPUT_MESSAGE;

    /**
     * This holds the locator for the error message while entering an e-mail.
     */
    @FindBy(jquery = "html body div#container div#content form#reg table tbody tr:eq(1) td span.invalid")
    private WebElement EMAIL_INPUT_MESSAGE;

    /**
     * This holds the locator for the error message while entering a telephone number.
     */
    @FindBy(jquery = "html body div#container div#content form#reg table tbody tr:eq(2) td span.invalid")
    private WebElement PHONE_INPUT_MESSAGE;

    /**
     * This holds the locator for the table where member after registration appears.
     */
    @FindBy(jquery = "table[class='simpletablestyle']")
    private WebElement TABLE_MEMBERS;

    /**
     * This holds the locator for the registration string after ("Registered!") after clicking on the register button.
     */
    @FindBy(jquery = "html body div#container div#content form#reg table tbody tr td ul.messages li.valid")
    private WebElement REGISTERED_MESSAGE;

    // STRINGS

    // NAME

    /**
     * Name of the member to register in the right format.
     */
    private final String NAME_FORMAT_OK = "John Doe";

    /**
     * Name of the member to register in the bad format.
     */
    private final String NAME_FORMAT_BAD_1 = ".";

    /**
     * Name of the member to register in the bad format.
     */
    private final String NAME_FORMAT_BAD_2 = "1";

    /**
     * Name of the member to register which is too long (1-25)
     */
    private final String NAME_FORMAT_TOO_LONG = "John Doe John Doe John Doe";

    // EMAIL

    /**
     * E-mail of the member to register in the right format.
     */
    private final String EMAIL_FORMAT_OK = "john@doe.com";

    /**
     * E-mail of the member to register in the bad format - #1.
     */
    private final String EMAIL_FORMAT_BAD_1 = "joe";

    /**
     * E-mail of the member to register in the bad format - #2.
     */
    private final String EMAIL_FORMAT_BAD_2 = "john@doe.com ";

    // PHONE

    /**
     * Phone number of the member to register in the right format.
     */
    private final String PHONE_FORMAT_OK = "0123456789";

    /**
     * Phone number of the member to register in the bad format.
     */
    private final String PHONE_FORMAT_BAD = "as/df.123@";

    /**
     * This method tests there is no new member in the registration table when all three input fields are empty.
     */
    @Test
    @InSequence(1)
    public void testEmptyRegistration() {
        open();

        assertTrue("Registration form is not present on the page", element(REGISTRATION_FORM).isVisible().apply(browser));

        guardHttp(REGISTRATION_FORM).submit();

        assertTrue(element(NAME_INPUT_MESSAGE).isVisible().apply(browser)
                && element(EMAIL_INPUT_MESSAGE).isVisible().apply(browser)
                && element(PHONE_INPUT_MESSAGE).isVisible().apply(browser)
                && !element(REGISTERED_MESSAGE).isVisible().apply(browser));
    }

    /**
     * This method tests registration of the new member with the name of bad formats.
     */
    @Test
    @InSequence(2)
    public void testRegistrationWithBadNameFormat() {
        open();
        setInputFields(NAME_FORMAT_BAD_1, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(REGISTER_BUTTON).click();
        assertTrue("Registration with bad name '" + NAME_FORMAT_BAD_1 + "' succeeded", element(NAME_INPUT_MESSAGE).isVisible()
                .apply(browser));

        open();
        setInputFields(NAME_FORMAT_BAD_2, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(REGISTER_BUTTON).click();
        assertTrue("Registration with bad name '" + NAME_FORMAT_BAD_2 + "' succeeded", element(NAME_INPUT_MESSAGE).isVisible()
                .apply(browser));

        open();
        setInputFields(NAME_FORMAT_TOO_LONG, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(REGISTER_BUTTON).click();
        assertTrue("Registration with bad name '" + NAME_FORMAT_TOO_LONG + "' succeeded", element(NAME_INPUT_MESSAGE)
                .isVisible().apply(browser));
    }

    /**
     * This method tests registration of the new member with the email of bad format.
     */
    @Test
    @InSequence(3)
    public void testRegistrationWithBadEmailFormat() {
        open();
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_1, PHONE_FORMAT_OK);
        guardHttp(REGISTER_BUTTON).click();
        assertTrue(element(EMAIL_INPUT_MESSAGE).isVisible().apply(browser));

        open();
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_2, PHONE_FORMAT_OK);
        guardHttp(REGISTER_BUTTON).click();
        assertTrue(element(EMAIL_INPUT_MESSAGE).isVisible().apply(browser));
    }

    /**
     * This method tests registration of the new member with the phone of bad format
     */
    @Test
    @InSequence(4)
    public void testRegistrationWithBadPhoneFormat() {
        open();
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD);
        guardHttp(REGISTER_BUTTON).click();
        assertTrue(element(PHONE_INPUT_MESSAGE).isVisible().apply(browser));
    }

    /**
     * This method tests regular registration process and tests JSON request by clicking at the JSON link of the newly
     * registered user.
     */
    @Test
    @InSequence(5)
    public void testRegularRegistration() {
        open();
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);

        guardHttp(REGISTER_BUTTON).click();

        assertTrue(element(REGISTERED_MESSAGE).isVisible().apply(browser));
        
        // extract table body from the member table
        WebElement rows = TABLE_MEMBERS.findElement(ByJQuery.jquerySelector("tbody"));

        // extract rows of the table body
        List<WebElement> members = rows.findElements(ByJQuery.jquerySelector("tr"));

        // assert we got 2 rows
        assertEquals(2, members.size());

        // get columns of the registered user
        List<WebElement> columns = members.get(0).findElements(ByJQuery.jquerySelector("td"));

        // assert we got 5 of them
        assertEquals(5, columns.size());

        // checks if we stored what we entered.
        assertTrue(columns.get(0).getText().equals("1"));
        assertTrue(columns.get(1).getText().equals(NAME_FORMAT_OK));
        assertTrue(columns.get(2).getText().equals(EMAIL_FORMAT_OK));
        assertTrue(columns.get(3).getText().equals(PHONE_FORMAT_OK));
    }

    /**
     * This helper method sets values into the according input fields.
     * 
     * @param name name to set into the name input field
     * @param email email to set into the email input field
     * @param phone phone to set into the phone input field
     */
    private void setInputFields(String name, String email, String phone) {
        NAME_FIELD.clear();
        NAME_FIELD.sendKeys(name);

        EMAIL_FIELD.clear();
        EMAIL_FIELD.sendKeys(email);

        PHONE_FIELD.clear();
        PHONE_FIELD.sendKeys(phone);
    }
}
