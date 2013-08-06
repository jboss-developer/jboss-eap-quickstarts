package org.jboss.as.quickstarts.jboss_as_cdi_portable_extension_drools;

import java.net.URL;
import javax.enterprise.inject.spi.Extension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.cdi.drools.extension.DroolsExtension;
import org.jboss.as.quickstarts.model.LineItem;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.thoughtworks.selenium.DefaultSelenium;

@RunWith(Arquillian.class)
public class CdiExtensionDroolsTest {

    @Deployment(testable = false)
    public static Archive<?> getDeployment() {
         File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve(
            "org.apache.deltaspike.core:deltaspike-core-api",
            "org.apache.deltaspike.core:deltaspike-core-impl",
            "org.drools:knowledge-api",
            "org.drools:drools-core",
            "org.drools:drools-compiler").withTransitivity().asFile();

        Archive<?> archive = ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackages(true, DroolsExtension.class.getPackage(),
                LineItem.class.getPackage())
            .addAsWebResource("index.xhtml")
            .addAsWebInfResource("beans.xml")
            .addAsWebInfResource("web.xml")
            .addAsWebInfResource(
                new StringAsset("<faces-config version=\"2.0\"/>"),
                "faces-config.xml")
            .addAsResource("SimpleRule.drl")
            .addAsServiceProvider(Extension.class, DroolsExtension.class)
            .addAsLibraries(libs);
        return archive;
    }

    @Drone
    DefaultSelenium browser;

    @ArquillianResource
    URL deploymentUrl;

    /*
     * It adds a LineItem of 24.00 --> no discount expected
     */

    @Test
    @RunAsClient
    @InSequence(1)
    public void assertFilledUnderOneHundred() {
        browser.open(deploymentUrl + "index.xhtml");
        browser.type("id=form:txtDescription", "demo");
        browser.type("id=form:txtPrice", "24");
        browser.click("id=form:btnBuy");
        browser.waitForPageToLoad("45000");
        Assert.assertTrue(browser
            .isElementPresent("xpath=//tr/td[contains(text(), '0.00')]"));

    }

    /*
     * It adds an other LineItem of 100.00 --> a discount of 10% is expected
     */
    @Test
    @RunAsClient
    @InSequence(2)
    public void assertFilledOverOneHundred() {

        browser.type("id=form:txtPrice", "100");
        browser.click("id=form:btnBuy");
        browser.waitForPageToLoad("45000");
        Assert.assertTrue(browser
            .isElementPresent("xpath=//tr/td[contains(text(), '2.40')]"));
    }

    /*
     * It adds an other LineItem of 100.00 --> a discount of 20% is expected
     */
    @Test
    @RunAsClient
    @InSequence(3)
    public void assertFilledOverTwoHundred() {

        browser.type("id=form:txtPrice", "100");
        browser.click("id=form:btnBuy");
        browser.waitForPageToLoad("45000");
        Assert.assertTrue(browser
            .isElementPresent("xpath=//tr/td[contains(text(), '4.80')]"));

    }

    /*
     * It removes last two LineItems --> a discount of 0% is expected
     */
    @Test
    @RunAsClient
    @InSequence(4)
    public void assertFilledNewlyUnderOneHundred() {

        browser.click("xpath=//tr[last()]/td/input");
        browser.click("xpath=//tr[last()]/td/input");
        browser.waitForPageToLoad("45000");
        Assert.assertTrue(browser
            .isElementPresent("xpath=//tr/td[contains(text(), '0.00')]"));
    }

}
