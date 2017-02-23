/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.picketlink;

import org.picketlink.common.exceptions.fed.WSTrustException;
import org.picketlink.common.util.DocumentUtil;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.w3c.dom.Element;

/**
 * This class demonstrates how to request SAML 2.0 security token from PicketLink STS.
 *
 * @author Peter Skopek (pskopek ( at redhat dot com))
 *
 */
public class WSTrustClientExample {

    public static void main(String[] args) throws Exception {

        String userName = (args.length > 0 ? args[0] : "tomcat");
        String password = (args.length > 1 ? args[1] : "tomcat");

        // Step 1: Create a WS Trust Client
        WSTrustClient client = new WSTrustClient("PicketLinkSTS", "PicketLinkSTSPort", "http://localhost:8080/picketlink-sts/PicketLinkSTS",
            new SecurityInfo(userName, password));
        Element assertionElement = null;
        try {
            System.out.println("Invoking token service to get SAML assertion for user:" + userName + " with password:" + password);
            // Step 2: Get a SAML2 Assertion Token from the PicketLink STS
            assertionElement = client.issueToken(SAMLUtil.SAML2_TOKEN_TYPE);
            System.out.println("SAML assertion for user:" + userName + " successfully obtained!");
        } catch (WSTrustException wse) {
            System.out.println("Unable to issue assertion: " + wse.getMessage());
            wse.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Problem:" + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }

        // Step 3: Display the SAML2 token
        String el = DocumentUtil.getDOMElementAsString(assertionElement);
        System.out.println(el);
    }

}
