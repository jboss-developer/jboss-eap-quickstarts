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
package org.jboss.as.quickstart.deltaspike.invocationhandler;

import javax.enterprise.context.ApplicationScoped;

/**
 * This class demonstrates the use of a DeltaSpike InvocationHandler
 * to provide a dynamic implementation of an interface.
 * 
 * When a method is called on this interface, the implementation will be
 * provided by the implementation class for @InvocationHandlerTestBinding.
 *
 */
@ApplicationScoped
@InvocationHandlerTestBinding
public interface InvocationHandlerTestInterface {
	String sayHello(String hello);
}
