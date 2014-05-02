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
package org.jboss.as.quickstarts.appclient.acc.client;

import javax.ejb.EJB;

import org.jboss.as.quickstarts.appclient.acc.client.interceptor.ClientInterceptor;
import org.jboss.as.quickstarts.appclient.server.ejb.StatelessSession;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.logging.Logger;

/**
 * A simple EJB client to demonstrate the use of the JBoss application container
 * to show that the injection will work for server EJB-beans.
 *  
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    @EJB
    private static StatelessSession slsb;

    /** Creates new form NewJFrame */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        EJBClientContext.getCurrent().registerInterceptor(0, new ClientInterceptor());
    	LOG.info(slsb.getGreeting());
    	slsb.invokeWithClientContext();
    }
}