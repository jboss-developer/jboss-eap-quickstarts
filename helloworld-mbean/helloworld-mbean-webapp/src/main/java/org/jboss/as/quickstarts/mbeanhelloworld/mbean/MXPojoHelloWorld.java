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
package org.jboss.as.quickstarts.mbeanhelloworld.mbean;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.as.quickstarts.mbeanhelloworld.service.HelloService;
import org.jboss.as.quickstarts.mbeanhelloworld.util.CDIExtension;

/**
 * Mbean pojo using MXBean interface and declared in jboss-service.xml.
 * 
 * @author Jeremie Lagarde
 * 
 */
public class MXPojoHelloWorld implements IHelloWorldMXBean {

    private String welcomeMessage = "Hello";
    private AtomicLong count = new AtomicLong(0);

    @Override
    public long getCount() {
        return count.get();
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
        count.incrementAndGet();
        HelloService helloService = CDIExtension.getBean(HelloService.class);
        return helloService.createHelloMessage(welcomeMessage, name);
    }
}