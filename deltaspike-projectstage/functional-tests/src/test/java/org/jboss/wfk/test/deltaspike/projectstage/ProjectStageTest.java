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
package org.jboss.wfk.test.deltaspike.projectstage;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests DeltaSpike projectstage-based bean inclusion
 *
 * @author Ron Smeral
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ProjectStageTest {

    @FindByJQuery("h2:contains('List of available CDI instances for MessageProvider') + table tbody tr td:first-child")
    List<WebElement> TABLE_FIRST_COLUMN;

    private Set<String> allMessages = new HashSet<String>();
    private static final String EXCLUDED_IF_NOT_DEVEL = "I should ALWAYS be available if the project stage is DEVELOPMENT";
    private static final String EXCLUDED_IF_DEVEL = "I should ALWAYS be available if project stage is NOT DEVELOPMENT";
    private static final String EXCLUDED_ALWAYS = "I should NEVER be available";
    private static final String EXCLUDED_NEVER = "I should ALWAYS be available";

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;

    private static final String TEST_DEPLOYMENT = "../target/jboss-deltaspike-projectstage.war";

    private static WebArchive deployment(String stage) {
        WebArchive war = ShrinkWrap.create(WebArchive.class, stage + ".war")
                .merge(ShrinkWrap.createFromZipFile(WebArchive.class, new File(TEST_DEPLOYMENT)));
        war.delete("/WEB-INF/classes/META-INF/apache-deltaspike.properties");
        war.addAsResource(new StringAsset("org.apache.deltaspike.ProjectStage=" + stage), "META-INF/apache-deltaspike.properties");
        return war;
    }

    @Deployment(name = "development", testable = false)
    public static WebArchive development() {
        return deployment("Development");
    }

    @Deployment(name = "staging", testable = false)
    public static WebArchive staging() {
        return deployment("Staging");
    }

    @Before
    public void beforeTest() {
        driver.navigate().to(contextPath);

        allMessages.clear();
        for (WebElement elem : TABLE_FIRST_COLUMN) {
            allMessages.add(elem.getText().trim());
        }
    }

    @Test
    @OperateOnDeployment("development")
    public void testDevelopment() {
        assertTrue(allMessages.contains(EXCLUDED_IF_NOT_DEVEL));
        assertTrue(allMessages.contains(EXCLUDED_NEVER));
        assertFalse(allMessages.contains(EXCLUDED_ALWAYS));
        assertFalse(allMessages.contains(EXCLUDED_IF_DEVEL));

    }

    @Test
    @OperateOnDeployment("staging")
    public void testStaging() {
        assertTrue(allMessages.contains(EXCLUDED_IF_DEVEL));
        assertTrue(allMessages.contains(EXCLUDED_NEVER));
        assertFalse(allMessages.contains(EXCLUDED_ALWAYS));
        assertFalse(allMessages.contains(EXCLUDED_IF_NOT_DEVEL));
    }
}