/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package org.jboss.as.quickstarts.wsba.coordinatorcompletion.simple;

import com.arjuna.mw.wst11.UserBusinessActivity;
import com.arjuna.mw.wst11.UserBusinessActivityFactory;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.wsba.coordinatorcompletion.simple.jaxws.SetServiceBA;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class ClientTest {

    @Inject
    @ClientStub
    public SetServiceBA client;

    @Deployment
    public static WebArchive createTestArchive() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsba.war")
                .addPackages(true, "org.jboss.as.quickstarts.wsba.coordinatorcompletion").addAsResource("context-handlers.xml")
                .addAsWebInfResource("web.xml", "web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

        /*
         * Remove the default MANIFEST.MF and replace with one that contains the required dependencies.
         */
        archive.delete(ArchivePaths.create("META-INF/MANIFEST.MF"));
        String ManifestMF = "Manifest-Version: 1.0\n"
                + "Dependencies: org.jboss.xts,deployment.arquillian-service,org.jboss.jts\n";
        archive.setManifest(new StringAsset(ManifestMF));

        return archive;
    }

    /**
     * Test the simple scenario where an item is added to the set within a Business Activity which is closed successfully.
     * 
     * @throws Exception if something goes wrong.
     */
    @Test
    public void testSuccess() throws Exception {
        UserBusinessActivity uba = UserBusinessActivityFactory.userBusinessActivity();
        try {
            String value1 = "1";
            String value2 = "2";

            uba.begin();

            client.addValueToSet(value1);
            client.addValueToSet(value2);

            uba.close();

            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value1));
            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value2));

        } finally {
            cancelIfActive(uba);
            client.clear();
        }
    }

    /**
     * Tests the scenario where an item is added to the set with in a business activity that is later cancelled. The test checks
     * that the item is in the set after invoking addValueToSet on the Web service. After cancelling the Business Activity, the
     * work should be compensated and thus the item should no longer be in the set.
     * 
     * @throws Exception if something goes wrong
     */
    @Test
    public void testCancel() throws Exception {
        UserBusinessActivity uba = UserBusinessActivityFactory.userBusinessActivity();
        try {
            String value1 = "1";
            String value2 = "2";

            uba.begin();

            client.addValueToSet(value1);
            client.addValueToSet(value2);

            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value1));
            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value2));

            uba.cancel();

            Assert.assertTrue("Expected value to not be in the set, but it was", !client.isInSet(value1));
            Assert.assertTrue("Expected value to not be in the set, but it was", !client.isInSet(value2));

        } finally {
            cancelIfActive(uba);
            client.clear();
        }

    }

    /**
     * This test attempts to add the same item to the set twice. An application exception is thrown and the Business Activity is
     * cancelled by the client.
     * 
     * @throws Exception
     */
    @Test(expected = AlreadyInSetException.class)
    public void testApplicationException() throws Exception {

        UserBusinessActivity uba = UserBusinessActivityFactory.userBusinessActivity();
        String value = "1";
        try {
            uba.begin();

            client.addValueToSet(value);
            client.addValueToSet(value);

        } finally {
            cancelIfActive(uba);
            client.clear();
        }

    }

    /**
     * Utility method for cancelling a Business Activity if it is currently active.
     * 
     * @param uba The User Business Activity to cancel.
     */
    private void cancelIfActive(UserBusinessActivity uba) {
        try {
            uba.cancel();
        } catch (Throwable th2) {
            // do nothing, already closed
        }
    }
}
