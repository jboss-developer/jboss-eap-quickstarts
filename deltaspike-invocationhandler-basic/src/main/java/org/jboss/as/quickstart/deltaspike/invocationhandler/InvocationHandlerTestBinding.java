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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.deltaspike.core.api.invocationhandler.annotation.InvocationHandlerBinding;

/**
 * This annotation binds a particular class (or interface) to 
 * an implementation class.
 * 
 * The implementation class must be annotated with this annotation, and it
 * must implement the java.lang.reflect.InvocationHandler interface.
 * 
 * It is critical that the annotation be available at runtime, via the
 * @Retention(RetentionPolicy.RUNTIME) annotation (as in the below example).
 */
@InvocationHandlerBinding
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvocationHandlerTestBinding {

}
