/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.quickstarts.ear.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.jboss.as.quickstarts.ear.client.GreeterEJBLocal;
import org.jboss.as.quickstarts.ear.client.GreeterException;


/**
 * @author bmaxwell
 *
 * @Named defaults to the name of the class with the first letter lower case so this bean can be referred to as greeterBean in a JSF page
 * It is RequestScoped so nothing is maintained beyond the request/response.
 */
@Named
@RequestScoped
public class GreeterBean {

    /* Inject the Local interface of the GreeterEJB so we can invoke sayHello */
    private @EJB GreeterEJBLocal greeterEJB;
    
    /* An instance variable to hold the data bound in the inputText of the JSF page */
    private String name;
    
    /* And instance variable to hold the EJB response or exception message that is bound to the outputText on the JSF page */
    private String response;
    
    /**
     * 
     */
    public GreeterBean() {
    }
    
    /**
     * The name getter to retrieve the name value which will be displayed in the inputText 
     * @return the name to be displayed in the inputText on the JSF page
     */
    public String getName() {
        return name;
    }

    /**
     * The name setter will be called when the user submits the JSF page and pass the value which can be usined in the sayHello action
     * @param name the string which is bound to the JSF page inputText
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The response displayed on the JSF page is an outputText, so we only need a getter
     * @return the value of the response variable which is set after the EJB is invoked in the sayHello action 
     */
    public String getResponse() {
        return response;
    }

    /**
     * This is a action which the JSF page will invoke when the Say Hello commandButton is clicked
     * @return a string which JSF uses for page navigation, an empty string means reload the original page
     */
    public String sayHello() {
        
        try {
            response = greeterEJB.sayHello(name);    
        } 
        catch(GreeterException e) {
            response = "Error: " + e.getMessage();
        }
        return "";
    }
    

}
