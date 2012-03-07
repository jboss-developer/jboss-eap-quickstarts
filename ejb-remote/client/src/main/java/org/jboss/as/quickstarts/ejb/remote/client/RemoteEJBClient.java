/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.as.quickstarts.ejb.remote.client;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.security.Security;
import java.util.Hashtable;

import org.jboss.as.quickstarts.ejb.remote.stateful.RemoteCounter;
import org.jboss.as.quickstarts.ejb.remote.stateless.RemoteCalculator;
import org.jboss.sasl.JBossSaslProvider;

/**
 * A sample program which acts a remote client for a EJB deployed on AS7 server.
 * This program shows how to lookup stateful and stateless beans via JNDI and then invoke on them
 *
 * @author Jaikiran Pai
 */
public class RemoteEJBClient {

    // The EJB invocation happens via the JBoss Remoting project, which uses SASL for
    // authentication for client-server authentication. There are various different security algorithms that
    // SASL supports. In this example we use "anonymous" access to the server and for that we register
    // the JBossSaslProvider which provides support for that algorithm. Depending on how which algorithm
    // is used, this piece of code to register JBossSaslProvider, may or may not be required
    static {
        Security.addProvider(new JBossSaslProvider());
    }

    public static void main(String[] args) throws Exception {
        // Invoke a stateless bean
        invokeStatelessBean();

        // Invoke a stateful bean
        invokeStatefulBean();
    }

    /**
     * Looks up a stateless bean and invokes on it
     *
     * @throws NamingException
     */
    private static void invokeStatelessBean() throws NamingException {
        // Let's lookup the remote stateless calculator
        final RemoteCalculator statelessRemoteCalculator = lookupRemoteStatelessCalculator();
        System.out.println("Obtained a remote stateless calculator for invocation");
        // invoke on the remote calculator
        int a = 204;
        int b = 340;
        System.out.println("Adding " + a + " and " + b + " via the remote stateless calculator deployed on the server");
        int sum = statelessRemoteCalculator.add(a, b);
        System.out.println("Remote calculator returned sum = " + sum);
        if (sum != a + b) {
            throw new RuntimeException("Remote stateless calculator returned an incorrect sum " + sum + " ,expected sum was " + (a + b));
        }
        // try one more invocation, this time for subtraction
        int num1 = 3434;
        int num2 = 2332;
        System.out.println("Subtracting " + num2 + " from " + num1 + " via the remote stateless calculator deployed on the server");
        int difference = statelessRemoteCalculator.subtract(num1, num2);
        System.out.println("Remote calculator returned difference = " + difference);
        if (difference != num1 - num2) {
            throw new RuntimeException("Remote stateless calculator returned an incorrect difference " + difference + " ,expected difference was " + (num1 - num2));
        }
    }

    /**
     * Looks up a stateful bean and invokes on it
     *
     * @throws NamingException
     */
    private static void invokeStatefulBean() throws NamingException {
        // Let's lookup the remote stateful counter
        final RemoteCounter statefulRemoteCounter = lookupRemoteStatefulCounter();
        System.out.println("Obtained a remote stateful counter for invocation");
        // invoke on the remote counter bean
        final int NUM_TIMES = 5;
        System.out.println("Counter will now be incremented " + NUM_TIMES + " times");
        for (int i = 0; i < NUM_TIMES; i++) {
            System.out.println("Incrementing counter");
            statefulRemoteCounter.increment();
            System.out.println("Count after increment is " + statefulRemoteCounter.getCount());
        }
        // now decrementing
        System.out.println("Counter will now be decremented " + NUM_TIMES + " times");
        for (int i = NUM_TIMES; i > 0; i--) {
            System.out.println("Decrementing counter");
            statefulRemoteCounter.decrement();
            System.out.println("Count after decrement is " + statefulRemoteCounter.getCount());
        }
    }

    /**
     * Looks up and returns the proxy to remote stateless calculator bean
     *
     * @return
     * @throws NamingException
     */
    private static RemoteCalculator lookupRemoteStatelessCalculator() throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);

      // The JNDI lookup name for a stateless session bean has the syntax of:
      // ejb:<appName>/<moduleName>/<distinctName>/<beanName>!<viewClassName>
      //
      // <appName> The application name is the name of the EAR that the EJB is deployed in 
      //           (without the .ear).  If the EJB JAR is not deployed in an EAR then this is
      //           blank.  The app name can also be specified in the EAR's application.xml
      //           
      // <moduleName> By the default the module name is the name of the EJB JAR file (without the
      //              .jar suffix).  The module name might be overridden in the ejb-jar.xml
      //
      // <distinctName> : AS7 allows each deployment to have an (optional) distinct name. 
      //                  This example does not use this so leave it blank.
      //
      // <beanName>     : The name of the session been to be invoked.
      //
      // <viewClassName>: The fully qualified classname of the remote interface.  Must include
      //                  the whole package name.

        // let's do the lookup
      return (RemoteCalculator) context.lookup(
         "ejb:/jboss-as-ejb-remote-app/CalculatorBean!" + RemoteCalculator.class.getName()
      );
    }

    /**
     * Looks up and returns the proxy to remote stateful counter bean
     *
     * @return
     * @throws NamingException
     */
    private static RemoteCounter lookupRemoteStatefulCounter() throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);

      // The JNDI lookup name for a stateful session bean has the syntax of:
      // ejb:<appName>/<moduleName>/<distinctName>/<beanName>!<viewClassName>?stateful
      //
      // <appName> The application name is the name of the EAR that the EJB is deployed in 
      //           (without the .ear).  If the EJB JAR is not deployed in an EAR then this is
      //           blank.  The app name can also be specified in the EAR's application.xml
      //           
      // <moduleName> By the default the module name is the name of the EJB JAR file (without the
      //              .jar suffix).  The module name might be overridden in the ejb-jar.xml
      //
      // <distinctName> : AS7 allows each deployment to have an (optional) distinct name. 
      //                  This example does not use this so leave it blank.
      //
      // <beanName>     : The name of the session been to be invoked.
      //
      // <viewClassName>: The fully qualified classname of the remote interface.  Must include
      //                  the whole package name.

      // let's do the lookup
      return (RemoteCounter) context.lookup(
         "ejb:/jboss-as-ejb-remote-app/CounterBean!" + RemoteCounter.class.getName()+"?stateful"
      );
    }
}
