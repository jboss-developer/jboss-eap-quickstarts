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
package org.jboss.as.quickstart.deltaspike.partialbean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.enterprise.context.ApplicationScoped;

/**
 * This class implements a dynamic DeltaSpike Partial Bean. It is bound to
 * one or more abstract classes or interfaces via the Binding Annotation
 * (@ExamplePartialBeanBinding below).
 * 
 * All abstract, unimplemented methods from those beans will be implemented
 * via the invoke method.
 * 
 */
@ExamplePartialBeanBinding
@ApplicationScoped
public class ExamplePartialBeanImplementation implements InvocationHandler {

    /**
     * In our example, this method will be invoked when the "sayHello" method is called.
     * 
     * @param proxy The object upon which the method is being invoked.
     * @param method The method being invoked (sayHello in this QuickStart)
     * @param args The arguments being passed in to the invoked method
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "Hello " + args[0];
    }
}
