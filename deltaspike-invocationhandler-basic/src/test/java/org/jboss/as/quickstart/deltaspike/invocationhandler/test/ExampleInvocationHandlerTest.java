/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstart.deltaspike.invocationhandler.test;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstart.deltaspike.invocationhandler.ExampleInvocationHandlerAbstractClass;
import org.jboss.as.quickstart.deltaspike.invocationhandler.ExampleInvocationHandlerInterface;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * Verification test.
 */
@RunWith(Arquillian.class)
public class ExampleInvocationHandlerTest {

    @Deployment
    public static Archive<?> getDeployment() {

        MavenDependencyResolver resolver = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

        Archive<?> archive = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, ExampleInvocationHandlerInterface.class.getPackage())
                .addAsLibraries(
                        resolver.artifacts("org.apache.deltaspike.core:deltaspike-core-api",
                                "org.apache.deltaspike.core:deltaspike-core-impl").resolveAsFiles())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return archive;
    }

    @Inject 
    private ExampleInvocationHandlerInterface exampleInvocationHandlerInterface;

    @Inject 
    private ExampleInvocationHandlerAbstractClass exampleInvocationHandlerAbstractClass;

    /**
     * Tests the InvocationHandler provided implementation of the "sayHello" method
     * on the ExampleInvocationHandlerInterface bean.
     */
    @Test
    public void testInvocationHandlerInterface() {
        String resultFromInterface = exampleInvocationHandlerInterface.sayHello("interface");
        assertEquals("Hello interface", resultFromInterface);
    }
    /**
     * Tests the InvocationHandler provided implementation of the "sayHello" method
     * on the ExampleInvocationHandlerAbstractClass bean.
     */
    @Test
    public void testInvocationHandlerAbstractClassInvocationHandlerMethod() {
    	String resultFromAbstractClass = exampleInvocationHandlerAbstractClass.sayHello("abstractclass");
        assertEquals("Hello abstractclass", resultFromAbstractClass);
    }
    /**
     * Tests the concrete implementation of the "otherHey" method on the 
     * ExampleInvocationHandlerAbstractClass bean.
     * 
     * This method's implementation will be provided by the class itself,
     * rather than by the InvocationHandler.
     */
    @Test
    public void testInvocationHandlerAbstractClassInvocationConcreteMethod() {
    	String resultFromAbstractClassConcreteMethod = exampleInvocationHandlerAbstractClass.otherHey("concretemethod");
        assertEquals("Other: concretemethod", resultFromAbstractClassConcreteMethod);
    }
}
