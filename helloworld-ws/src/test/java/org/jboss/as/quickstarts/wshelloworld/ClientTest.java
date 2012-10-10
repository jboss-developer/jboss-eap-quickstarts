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
package org.jboss.as.quickstarts.wshelloworld;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.as.quickstarts.wshelloworld.HelloWorldService;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Simple set of tests for the HelloWorld Web Service to demonstrate accessing the web service using a client
 * 
 * Note: These tests rely on the Web Service already running
 * 
 * @author lnewson@redhat.com
 */
public class ClientTest {

    private static HelloWorldService client;

    @BeforeClass
    public static void setup() {
        try {
            client = new Client();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHello() {
        assertEquals(client.sayHello(), "Hello World!");
    }

    @Test
    public void testHelloName() {
        assertEquals(client.sayHelloToName("John"), "Hello John!");
    }

    @Test
    public void testHelloNames() {
        final List<String> names = new ArrayList<String>();
        names.add("John");
        names.add("Mary");
        names.add("Mark");

        assertEquals(client.sayHelloToNames(names), "Hello John, Mary & Mark!");
    }
}
