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
package org.jboss.as.quickstarts.mbeanhelloworld.mbean;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.as.quickstarts.mbeanhelloworld.service.HelloService;

/**
 * Mbean component using .MXBean interface.
 * 
 * @author Jérémie Lagarde
 * 
 */
@Singleton
@Startup
public class MXComponentHelloWorld extends AbstractComponentMBean implements IHelloWorldMXBean {

    private String welcomeMessage = "Hello";
    private long count = 0;

    @Inject
    HelloService helloService;

    public MXComponentHelloWorld() {
        super("quickstarts");
    }
    
    @Override
    public long getCount() {
        return count;
    }

    @Override
    public void setWelcomeMessage(String message) {
        if (message != null && message.trim().length() > 0)
            welcomeMessage = message;
    }

    @Override
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    @Override
    public String sayHello(String name) {
        count++;
        return helloService.createHelloMessage(welcomeMessage, name);
    }

}