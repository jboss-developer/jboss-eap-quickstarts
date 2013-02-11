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
package org.jboss.as.quickstarts.invocationhandler;

import java.util.Arrays;
import java.util.logging.Logger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.core.api.invocationhandler.annotation.InvocationHandlerBinding;

@HelloWorldInvocationHandlerBinding
@ApplicationScoped
public class HelloWorldInvocationHandler implements InvocationHandler {
    private static final Logger log = Logger.getLogger(HelloWorldInvocationHandler.class.getName());

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
    {
    	log.info("Method called: " + method.getName());
    	log.info("Arguments passed: " + Arrays.asList(args));
        return "Hello " + args[0];
    }
}
