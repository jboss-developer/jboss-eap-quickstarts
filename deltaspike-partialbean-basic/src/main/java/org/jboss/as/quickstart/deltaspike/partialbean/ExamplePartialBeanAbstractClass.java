/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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

import javax.enterprise.context.ApplicationScoped;

/**
 * This class demonstrates the use of a DeltaSpike InvocationHandler
 * to provide a dynamic implementation of a partial bean.
 * 
 * For this example, the partial bean is provided by an abstract class. A 
 * binding annotation (@ExamplePartialBeanBinding in this case) is placed 
 * on this class in order to bind the abstract methods to an implementation class.
 * 
 * The abstract class may provide both abstract methods (which will be replaced
 * by the InvocationHandler at Runtime) as well as concrete methods, whose 
 * code will be provided in the class below.
 *
 */
@ApplicationScoped
@ExamplePartialBeanBinding
public abstract class ExamplePartialBeanAbstractClass {
    /**
     * This abstract method will be provided by the InvocationHandler's invoke
     * method.
     */
    public abstract String sayHello(String hello);
    
    /**
     * In this case, the concrete implementation below will be called.
     */
    public String otherHey (String hello) {
        return "Other: " + hello;
    }
}
