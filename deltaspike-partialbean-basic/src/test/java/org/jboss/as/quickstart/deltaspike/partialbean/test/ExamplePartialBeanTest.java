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
package org.jboss.as.quickstart.deltaspike.partialbean.test;

import java.io.File;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstart.deltaspike.partialbean.ExamplePartialBeanAbstractClass;
import org.jboss.as.quickstart.deltaspike.partialbean.ExamplePartialBeanInterface;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Verification test.
 */
@RunWith(Arquillian.class)
public class ExamplePartialBeanTest {

    @Deployment
    public static Archive<?> getDeployment() {

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve(
                "org.apache.deltaspike.core:deltaspike-core-api", 
                "org.apache.deltaspike.core:deltaspike-core-impl",
                "org.apache.deltaspike.modules:deltaspike-partial-bean-module-impl"
        ).withTransitivity().asFile();

        Archive<?> archive = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, ExamplePartialBeanInterface.class.getPackage())
                .addAsLibraries(libs)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return archive;
    }

    @Inject 
    private ExamplePartialBeanInterface examplePartialBeanInterface;

    @Inject 
    private ExamplePartialBeanAbstractClass examplePartialBeanAbstractClass;

    /**
     * Tests the InvocationHandler provided implementation of the "sayHello" method
     * on the ExamplePartialBeanInterface bean.
     */
    @Test
    public void testInvocationHandlerInterface() {
        String resultFromInterface = examplePartialBeanInterface.sayHello("interface");
        assertEquals("Hello interface", resultFromInterface);
    }
    /**
     * Tests the InvocationHandler provided implementation of the "sayHello" method
     * on the ExamplePartialBeanAbstractClass bean.
     */
    @Test
    public void testInvocationHandlerAbstractClassInvocationHandlerMethod() {
        String resultFromAbstractClass = examplePartialBeanAbstractClass.sayHello("abstractclass");
        assertEquals("Hello abstractclass", resultFromAbstractClass);
    }
    /**
     * Tests the concrete implementation of the "otherHey" method on the 
     * ExamplePartialBeanAbstractClass bean.
     * 
     * This method's implementation will be provided by the class itself,
     * rather than by the InvocationHandler.
     */
    @Test
    public void testInvocationHandlerAbstractClassInvocationConcreteMethod() {
        String resultFromAbstractClassConcreteMethod = examplePartialBeanAbstractClass.otherHey("concretemethod");
        assertEquals("Other: concretemethod", resultFromAbstractClassConcreteMethod);
    }
}
