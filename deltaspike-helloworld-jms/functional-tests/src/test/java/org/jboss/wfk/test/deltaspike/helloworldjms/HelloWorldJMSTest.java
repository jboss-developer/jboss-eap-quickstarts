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
package org.jboss.wfk.test.deltaspike.helloworldjms;

import java.io.File;
import java.net.URL;
import java.util.List;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests DeltaSpike HelloWorld JMS quickstart
 *
 * @author Ron Smeral
 */
@RunAsClient
@RunWith(Arquillian.class)
public class HelloWorldJMSTest {

    private static final String DEF_MSG_CONTENT = "Hello, World from Deltaspike!";
    private static final String DEF_MSG_COUNT = "1";
    private static final String NEW_MSG_CONTENT = "Hello, Test!";
    private static final int NEW_MSG_COUNT = 3;
    private static final String MSG_FORMAT = "Received message #%d with content: %s";
    private static final String ATTR_VALUE = "value";

    @FindByJQuery("ul[id='messages'] li")
    List<WebElement> MESSAGES;

    @FindByJQuery("input[id $= 'messageContent']")
    WebElement INPUT_MESSAGE_CONTENT;

    @FindByJQuery("input[id $= 'messageCount']")
    WebElement INPUT_MESSAGE_COUNT;

    @FindByJQuery("input[id $= 'sendMessages']")
    WebElement BTN_SEND;

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;

    private static final String DEPLOYMENT_WAR = "../target/jboss-deltaspike-helloworld-jms.war";

    @Deployment(testable = false)
    public static WebArchive deployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT_WAR)).addAsWebInfResource("test-queue-hornetq-jms.xml");
    }

    @BeforeClass
    public static void waitForQueue() throws Exception {
        // make sure the queue is deployed
        Thread.sleep(10000);
    }

    @Before
    public void loadContext() {
        driver.navigate().to(contextPath);
    }

    @Test
    @InSequence(1)
    public void loadTest() {
        assertTrue(INPUT_MESSAGE_CONTENT.getAttribute(ATTR_VALUE).trim().contains(DEF_MSG_CONTENT));
        assertTrue(INPUT_MESSAGE_COUNT.getAttribute(ATTR_VALUE).trim().contains(DEF_MSG_COUNT));
    }

    @Test
    @InSequence(2)
    public void messageTest() {
        INPUT_MESSAGE_CONTENT.clear();
        INPUT_MESSAGE_CONTENT.sendKeys(NEW_MSG_CONTENT);

        INPUT_MESSAGE_COUNT.clear();
        INPUT_MESSAGE_COUNT.sendKeys(Integer.toString(NEW_MSG_COUNT));

        guardHttp(BTN_SEND).click();

        assertTrue(INPUT_MESSAGE_CONTENT.getAttribute(ATTR_VALUE).trim().contains(NEW_MSG_CONTENT));
        assertTrue(INPUT_MESSAGE_COUNT.getAttribute(ATTR_VALUE).trim().contains(Integer.toString(NEW_MSG_COUNT)));
        for (int i = 0; i < MESSAGES.size(); i++) {
            assertEquals(String.format(MSG_FORMAT, i + 1, NEW_MSG_CONTENT), MESSAGES.get(i).getText().trim());
        }
    }
}