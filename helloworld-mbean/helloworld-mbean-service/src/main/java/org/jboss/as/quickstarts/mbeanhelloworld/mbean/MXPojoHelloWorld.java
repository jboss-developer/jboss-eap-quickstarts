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

/**
 * Mbean pojo using MXBean interface and declared in jboss-service.xml.
 * 
 * @author Jérémie Lagarde
 * 
 */
public class MXPojoHelloWorld implements IHelloWorldMXBean {

    private String welcomeMessage = "Hello";
    private long count = 0;

    public long getCount() {
        return count;
    }

    public void setWelcomeMessage(String message) {
        if (message != null && message.trim().length() > 0)
            welcomeMessage = message;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public String sayHello(String name) {
        count++;
        return welcomeMessage + " " + name + "!";
    }

    public void start() throws Exception {
        System.out.println(" >> MXPojoHelloWorld.start() invoked");
    }

    public void stop() throws Exception {
        System.out.println(" << MXPojoHelloWorld.stop()  invoked");
    }

}