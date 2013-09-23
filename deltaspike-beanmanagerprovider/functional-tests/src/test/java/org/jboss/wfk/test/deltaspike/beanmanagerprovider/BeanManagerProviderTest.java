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
package org.jboss.wfk.test.deltaspike.beanmanagerprovider;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;
import java.io.File;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.openqa.selenium.support.FindBy;

/**
 * Tests DeltaSpike BeanManagerProvider
 *
 * @author Ron Smeral
 */
@RunAsClient
@RunWith(Arquillian.class)
public class BeanManagerProviderTest {

    @FindByJQuery("ul li:contains('New Contact added')")
    WebElement MSG_CONTACT_ADDED;

    @FindByJQuery("ul li:contains('Contact Removed')")
    WebElement MSG_CONTACT_REMOVED;

    @FindByJQuery("ul li:contains('Contact updated')")
    WebElement MSG_CONTACT_UPDATED;

    @FindByJQuery("ul li:contains('Can't create contact:')")
    WebElement MSG_CONTACT_ERROR;

    @FindByJQuery("input[id*='inputnameValue']")
    WebElement INPUT_NAME;

    @FindByJQuery("input[id*='inputemailValue']")
    WebElement INPUT_EMAIL;

    @FindByJQuery("input[id*='inputphoneValue']")
    WebElement INPUT_PHONE;

    @FindByJQuery("span[id*='inputnameMessage']")
    WebElement MSG_BV_NAME;

    @FindByJQuery("span[id*='inputemailMessage']")
    WebElement MSG_BV_EMAIL;

    @FindByJQuery("span[id*='inputphoneMessage']")
    WebElement MSG_BV_PHONE;

    @FindByJQuery("input[value='Save']")
    WebElement BTN_SAVE;

    @FindByJQuery("input[value='Save'] + input[value='Remove']")
    WebElement BTN_REMOVE;

    @FindByJQuery("input[value='New Contact']")
    WebElement BTN_NEW_CONTACT;

    private static final String XPATH_TABLE_CONTACTS = "//form[preceding-sibling::h2[contains(text(), 'All Contacts')]]/table/tbody";

    @FindBy(xpath = XPATH_TABLE_CONTACTS)
    WebElement TABLE_CONTACTS;

    @FindByJQuery("h2:contains('Audit Records') + table tbody")
    WebElement TABLE_AUDIT;

    private static final String NAME1 = "Foo";
    private static final String NAME2 = "Fool";
    private static final String EMAIL1 = "foo@bar.com";
    private static final String PHONE1 = "1234567890";
    private static final String PHONE2 = "12345678901";
    private static final String PHONE_LONG = "123456789012345";
    private static final String ERR_VALUE_REQUIRED = "Validation Error: Value is required.";
    private static final String ERR_SIZE_10_12 = "size must be between 10 and 12";
    private static final String ERR_OUT_OF_BOUNDS = "numeric value out of bounds";

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;

    private static final String TEST_DEPLOYMENT = "../target/jboss-deltaspike-beanmanagerprovider.war";

    @Deployment(testable = false)
    public static WebArchive deployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(TEST_DEPLOYMENT));
    }

    @Before
    public void beforeTest() {
        driver.navigate().to(contextPath);
    }

    @Test
    @InSequence(1)
    public void testContactCreatedAndLogged() throws InterruptedException {
        enterContact(NAME1, EMAIL1, PHONE1, BTN_SAVE);

        assertTrue(MSG_CONTACT_ADDED.isDisplayed());
        assertTrue(tableRowEquals(TABLE_CONTACTS, 1, null, NAME1, EMAIL1, PHONE1));
        assertTrue(tableRowEquals(TABLE_AUDIT, 0, EMAIL1, NAME1, PHONE1, null, "INSERT"));
    }

    @Test
    @InSequence(2)
    public void testDuplicateContact() throws InterruptedException {
        enterContact(NAME1, EMAIL1, PHONE1, BTN_SAVE);

        assertTrue(MSG_CONTACT_ERROR.isDisplayed());
        assertEquals(1, TABLE_AUDIT.findElements(By.tagName("tr")).size());
    }

    @Test
    @InSequence(3)
    public void testContactValidationError() {
        enterContact("", "", PHONE_LONG, BTN_SAVE);

        // Make sure the page is loaded
        waitModel().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return MSG_BV_PHONE.getText().contains(ERR_SIZE_10_12) || MSG_BV_PHONE.getText().contains(ERR_OUT_OF_BOUNDS);
            }
        });

        assertFalse(new WebElementConditionFactory(MSG_CONTACT_ADDED).isPresent().apply(driver));
        assertTrue(MSG_BV_NAME.getText().contains(ERR_VALUE_REQUIRED));
        assertTrue(MSG_BV_EMAIL.getText().contains(ERR_VALUE_REQUIRED));
        assertTrue(MSG_BV_PHONE.getText().contains(ERR_SIZE_10_12) || MSG_BV_PHONE.getText().contains(ERR_OUT_OF_BOUNDS));
    }

    @Test
    @InSequence(4)
    public void testNoValidationErrorIfNotSaved() {
        enterContact("", "", "", BTN_NEW_CONTACT);

        assertFalse(MSG_BV_NAME.getText().contains(ERR_VALUE_REQUIRED));
        assertFalse(MSG_BV_EMAIL.getText().contains(ERR_VALUE_REQUIRED));
        assertFalse(MSG_BV_PHONE.getText().contains(ERR_OUT_OF_BOUNDS));
    }

    @Test
    @InSequence(5)
    public void testUpdateContact() throws InterruptedException {
        Thread.sleep(1000);
        tableRowSelectForEdit(1);
        enterContact(NAME2, null, PHONE2, BTN_SAVE);

        assertTrue(MSG_CONTACT_UPDATED.isDisplayed());
        assertTrue(tableRowEquals(TABLE_CONTACTS, 1, null, NAME2, EMAIL1, PHONE2));
        assertTrue(tableRowEquals(TABLE_AUDIT, 1, EMAIL1, NAME2, PHONE2, null, "UPDATE"));
    }

    @Test
    @InSequence(6)
    public void testDeleteContact() throws InterruptedException {
        Thread.sleep(1000);
        tableRowRemove(1);

        assertTrue(MSG_CONTACT_REMOVED.isDisplayed());
        assertEquals(1, TABLE_CONTACTS.findElements(By.tagName("tr")).size());
        assertTrue(tableRowEquals(TABLE_AUDIT, 2, EMAIL1, NAME2, PHONE2, null, "DELETE"));
    }

    private boolean tableRowEquals(WebElement table, int rowIndex, String... expectedCellContents) {
        List<WebElement> rowCells = table.findElements(By.xpath(String.format("tr[%d]/td", rowIndex + 1)));
        if (expectedCellContents.length > rowCells.size()) {
            return false;
        }
        int i = 0;
        while (i < expectedCellContents.length && (expectedCellContents[i] == null
                || expectedCellContents[i].equals(rowCells.get(i).getText().trim()))) {
            i++;
        }
        return i == expectedCellContents.length;
    }

    private void tableRowRemove(int rowIndex) throws InterruptedException {
        int contactId = Integer.parseInt(TABLE_CONTACTS.findElement(By.xpath(String.format("tr[%d]/td[1]", rowIndex + 1))).getText().trim());
        TABLE_CONTACTS.findElement(By.xpath(String.format("tr[%d]/td/input[@value='Remove']", rowIndex + 1))).click();

        // Ugly indeed, but there's currently no way to wait for an Alert to appear
        Thread.sleep(1000);

        driver.switchTo().alert().accept();
        driver.switchTo().defaultContent();
        waitModel().until().element(By.xpath(String.format(XPATH_TABLE_CONTACTS + "/tr/td[1][contains(text(), '%d')]", contactId))).is().not().present();
    }

    private void tableRowSelectForEdit(int rowIndex) {
        guardHttp(TABLE_CONTACTS.findElement(By.xpath(String.format("//tr[%d]/td/input[@value='Select for edit']", rowIndex + 1)))).click();
    }

    private void enterContact(String name, String email, String phone, WebElement button) {
        if (name != null) {
            INPUT_NAME.clear();
            INPUT_NAME.sendKeys(name);
        }

        if (email != null) {
            INPUT_EMAIL.clear();
            INPUT_EMAIL.sendKeys(email);
        }

        if (phone != null) {
            INPUT_PHONE.clear();
            INPUT_PHONE.sendKeys(phone);
        }

        guardHttp(button).click();
    }
}
