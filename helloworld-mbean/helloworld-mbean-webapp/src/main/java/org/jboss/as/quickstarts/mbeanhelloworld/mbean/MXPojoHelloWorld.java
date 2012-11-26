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

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstarts.mbeanhelloworld.service.HelloService;

/**
 * Mbean pojo using MXBean interface and declared in jboss-service.xml.
 * 
 * @author Jérémie Lagarde
 * 
 */
public class MXPojoHelloWorld  implements IHelloWorldMXBean  {

    private String welcomeMessage = "Hello";
    private long count = 0;

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
        Context context = null;
        BeanManager beanManager = null;
        HelloService helloService;
        try {
            context = new InitialContext();
            beanManager = (BeanManager) context.lookup("java:comp/BeanManager");
            @SuppressWarnings("unchecked")
            final Bean<HelloService> bean = (Bean<HelloService>) beanManager.getBeans(HelloService.class).iterator().next(); 
            final CreationalContext<HelloService> ctx = beanManager.createCreationalContext(bean);
            helloService = (HelloService) beanManager.getReference(bean, bean.getClass(), ctx);
            return helloService.createHelloMessage(welcomeMessage, name);
        } catch (NamingException e) {
            e.printStackTrace();
        }finally {
            if (context != null) {
                try {
                    context.close();
                }
                catch (NamingException e) {
                }
            }
        }
        return null;
    }
}