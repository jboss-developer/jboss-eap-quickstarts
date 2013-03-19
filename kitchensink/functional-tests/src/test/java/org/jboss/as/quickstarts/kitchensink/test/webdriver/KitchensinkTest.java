/**
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
package org.jboss.as.quickstarts.kitchensink.test.webdriver;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This class provides Arquillian-related infrastructure and functional tests afterwards.
 *
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
@RunWith(Arquillian.class)
public class KitchensinkTest {

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    protected URL contextPath;

    /**
     * Injects browser to our test.
     */
    @Drone
    protected WebDriver browser;

    /**
     * Specifies relative path to the war of built application in the main project.
     */
    private static final String DEPLOYMENT_WAR = "../target/jboss-as-kitchensink.war";

    /**
     * Creates deployment which is sent to the container upon test's start.
     *
     * @return war file which is deployed while testing, the whole application in our case
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT_WAR));
    }

    /**
     * Locator for registration form
     */
    @FindBy(id = "reg")
    private WebElement REGISTRATION_FORM;

    /**
     * Locator for the input field for entering of a name of the member we want to register.
     */
    @FindBy(id = "reg:name")
    private WebElement NAME_FIELD;

    /**
     * Locator for the input field for entering of an e-mail of the new member we want to register.
     */
    @FindBy(id = "reg:email")
    private WebElement EMAIL_FIELD;

    /**
     * Locator for the input field for entering of a telephone number of the new member we want to register.
     */
    @FindBy(id = "reg:phoneNumber")
    private WebElement PHONE_FIELD;

    /**
     * This class variable holds locator for the registration button we click on while registering a new member.
     */
    @FindBy(id = "reg:register")
    private WebElement REGISTER_BUTTON;

    @FindBy(jquery = ".invalid")
    private List<WebElement> INVALID;

    /**
     * This holds the locator for the table where member after registration appears.
     */
    @FindBy(css = ".simpletablestyle")
    private WebElement TABLE_MEMBERS;

    /**
     * This holds the locator for the registration string after ("Registered!") after clicking on the register button.
     */
    @FindBy(jquery = ".valid")
    private WebElement REGISTERED_MESSAGE;

    /**
     * Name of the member to register in the right format.
     */
    private final String NAME_FORMAT_OK = "John Doe";

    /**
     * Name of the member to register in the bad format.
     */
    private final String NAME_FORMAT_BAD = "123";

    /**
     * Name of the member to register which is too long (1-25)
     */
    private final String NAME_FORMAT_TOO_LONG = "John Doe John Doe John Doe";

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

    /**
     * Phone number of the member to register in the right format.
     */
    private final String PHONE_FORMAT_OK = "0123456789";

    /**
     * Phone number of the member to register in the bad format.
     */
    private final String PHONE_FORMAT_BAD = "as/df.123@12";

    /**
     * Phone number of the member to register which is too small.
     */
    private final String PHONE_FORMAT_TOO_SMALL = "1";

    /**
     * Phone number of the member to register which is too long.
     */
    private final String PHONE_FORMAT_TOO_LONG = "1234567890123";

    @Test
    @InSequence(1)
    @Ignore("skipped until JDF-255 is resolved")
    public void testEmptyRegistration() {
        open();

        Graphene.waitModel(browser).until().element(REGISTRATION_FORM).is().visible();

        assertTrue("Registration form is not present on the page", element(REGISTRATION_FORM).isVisible().apply(browser));

        Graphene.guardHttp(REGISTRATION_FORM).submit();

        assertTrue(contains(INVALID, "size must be between 1 and 25"));
        assertTrue(contains(INVALID, "may not be empty"));
        assertTrue(contains(INVALID, "numeric value out of bounds (<12 digits>.<0 digits> expected)"));

        assertTrue(!element(REGISTERED_MESSAGE).isPresent().apply(browser));
    }

    @Test
    @InSequence(2)
    public void testRegistrationWithBadNameFormat() throws InterruptedException {
        open();

        setInputFields(NAME_FORMAT_BAD, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);

        Graphene.guardHttp(REGISTER_BUTTON).click();

        assertTrue("Registration with bad name '" + NAME_FORMAT_BAD + "' succeeded",
            contains(INVALID, "Must not contain numbers"));
    }

    @Test
    @InSequence(3)
    public void testRegistrationWithTooLongName() {
        open();

        setInputFields(NAME_FORMAT_TOO_LONG, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);

        Graphene.guardHttp(REGISTER_BUTTON).click();

        assertTrue("Registration with bad name '" + NAME_FORMAT_TOO_LONG + "' succeeded",
            contains(INVALID, "size must be between 1 and 25"));
    }

    @Test
    @InSequence(4)
    public void testRegistrationWithBadEmailFormat() {
        open();

        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_1, PHONE_FORMAT_OK);

        Graphene.guardHttp(REGISTRATION_FORM).submit();

        assertTrue(contains(INVALID, "not a well-formed email address"));
    }

    @Test
    @InSequence(5)
    public void testRegistrationWithBadEmailFormat2() {
        open();
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_2, PHONE_FORMAT_OK);
        Graphene.guardHttp(REGISTRATION_FORM).submit();
        assertTrue(contains(INVALID, "not a well-formed email address"));
    }

    @Test
    @InSequence(6)
    public void testRegistrationWithTooSmallPhoneNumber() {
        open();

        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_TOO_SMALL);

        Graphene.guardHttp(REGISTRATION_FORM).submit();

        assertTrue(contains(INVALID, "size must be between 10 and 12"));
    }

    @Test
    @InSequence(7)
    @Ignore("skipped until JDF-255 is resolved")
    public void testRegistrationWithTooLongPhoneNumber() {
        open();

        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_TOO_LONG);

        Graphene.guardHttp(REGISTRATION_FORM).submit();

        assertTrue(contains(INVALID, "numeric value out of bounds (<12 digits>.<0 digits> expected)"));
    }

    @Test
    @InSequence(8)
    public void testRegistrationWithBadPhoneNumber() {
        open();

        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD);

        Graphene.guardHttp(REGISTRATION_FORM).submit();

        assertTrue(contains(INVALID, "numeric value out of bounds (<12 digits>.<0 digits> expected)"));
    }

    @Test
    @InSequence(9)
    public void testRegularRegistration() {
        open();
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);

        Graphene.guardHttp(REGISTER_BUTTON).click();

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
     * Helper method which opens the main page for us.
     */
    public void open() {
        browser.get(contextPath.toString());
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

    /**
     * Helper method which checks if some list of WebElemets contains an element which holds some particular message.
     *
     * @param elements list of elements to check the existence of a message of
     * @param message
     * @return true if list of elemets contains the message, false otherwise
     */
    private boolean contains(final List<WebElement> elements, String message) {
        for (WebElement element : elements) {
            if (element.getText().equals(message)) {
                return true;
            }
        }
        return false;
    }
}