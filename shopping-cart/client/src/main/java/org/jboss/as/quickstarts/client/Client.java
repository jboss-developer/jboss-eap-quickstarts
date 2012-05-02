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
package org.jboss.as.quickstarts.client;

import java.util.HashMap;
import java.util.Hashtable;

import javax.ejb.NoSuchEJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstarts.sfsb.ShoppingCart;
import org.jboss.as.quickstarts.sfsb.ShoppingCartBean;

public class Client {
    
    private static final String SOAP = "JBoss SOA Platform 6";
    private static final String EAP = "JBoss Enterprise Application Platform 6";

    public static void main(String[] args) throws NamingException {

        // Create the JNDI InitialContext, configuring it for use with JBoss EJB
        Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);

        /*
         * The app name is the application name of the deployed EJBs. This is typically the ear name without the .ear suffix.
         * However, the application name could be overridden in the application.xml of the EJB deployment on the server. Since
         * we haven't deployed the application as a .ear, the app name for us will be an empty string
         */
        final String appName = "";

        /*
         * This is the module name of the deployed EJBs on the server. This is typically the jar name of the EJB deployment,
         * without the .jar suffix, but can be overridden via the ejb-jar.xml. In this example, we have deployed the EJBs in a
         * jboss-as-shoppingcart-server.jar, so the module name is jboss-as-shopping-cart-server
         */
        final String moduleName = "jboss-as-shoppingcart-server";

        /*
         * AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for our EJB
         * deployment, so this is an empty string
         */
        final String distinctName = "";

        /*
         * The EJB name which by default is the simple class name of the bean implementation class
         */
        final String beanName = ShoppingCartBean.class.getSimpleName();

        /* The remote view fully qualified class name */
        final String viewClassName = ShoppingCart.class.getName();

        /* Lookup the remote interface of the shopping cart */
        String lookupName = "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName
                + "?stateful";
        final ShoppingCart cart = (ShoppingCart) context.lookup(lookupName);

        System.out.println("\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("Obtained the remote interface to the shopping cart");

        /* invoke on the remote interface */
        System.out.println("Buying a \"" + EAP + "\"");
        cart.buy(EAP, 1);
        System.out.println("Buying another \"" + EAP + "\"");
        cart.buy(EAP, 1);

        System.out.println("Buying a \"" + SOAP + "\"");
        cart.buy(SOAP, 1);
        
        System.out.println("\nPrint cart:");
        HashMap<String, Integer> cartContents = cart.getCartContents();
        for (String product : cartContents.keySet()) {
            System.out.println(cartContents.get(product) + "     " + product);
        }
        
        System.out.println("\nCheckout");
        cart.checkout();
        
        /* Try to access the cart after checkout*/
        try {
            cart.getCartContents();
        } catch (NoSuchEJBException e) {
            System.out.println("ERROR: Cannot access cart after Checkout!");
        }
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n");
    }
}
