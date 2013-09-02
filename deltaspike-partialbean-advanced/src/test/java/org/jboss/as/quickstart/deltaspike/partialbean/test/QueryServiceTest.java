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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstart.deltaspike.partialbeanadvanced.model.Person;
import org.jboss.as.quickstart.deltaspike.partialbeanadvanced.queryservice.PersonQueryService;
import org.jboss.as.quickstart.deltaspike.partialbeanadvanced.util.Resources;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verification test.
 */
@RunWith(Arquillian.class)
public class QueryServiceTest {

    @Deployment
    public static Archive<?> getDeployment() {

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve(
                "org.apache.deltaspike.core:deltaspike-core-api", 
                "org.apache.deltaspike.core:deltaspike-core-impl",
                "org.apache.deltaspike.modules:deltaspike-partial-bean-module-impl"
        ).withTransitivity().asFile();

        Archive<?> archive = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, Person.class.getPackage())
                .addPackages(true, PersonQueryService.class.getPackage())
                .addPackages(true, Resources.class.getPackage())
                .addAsLibraries(libs)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml");
        return archive;
    }

    // sorted list of all Person entries in the default sql
    private Person[] allPerson = new Person[] {
        new Person("lightguard_jp", "Jason Porter"),
        new Person("mbg", "Marius Bogoevici"),
        new Person("pmuir", "Pete Muir"),
        new Person("rbenevides", "Rafael Benevides"),
        new Person("sgilda", "Sande Gilda")
    };
    
    @Inject
    private PersonQueryService personQueryService;
    
    /**
     * Run find all and confirm that all results are present.
     */
    @Test
    public void testFindAll() {
        List<Person> personList = personQueryService.findAll();
        assertEquals("List sizes should match", personList.size(), allPerson.length);
        for (Person p : allPerson) {
            assertTrue("Results should contain all entries", personList.contains(p));
        }
    }
    
    /**
     * Insure that findAllSortedByName returns a sorted list of the correct size.
     */
    @Test
    public void testFindAllSorted() {
        List<Person> personList = personQueryService.findAllSortedByName();
        assertArrayEquals("Arrays should be equal", allPerson, personList.toArray());
    }

    /**
     * Runs a test to insure that filtering by person.name works correctly.
     */
    @Test
    public void testFindByName() {
        List<Person> personList = personQueryService.findByName("Rafael Benevides");
        assertEquals("Array length should be 1", 1, personList.size());
        assertEquals("Person's nick should be 'rbenevides'", "rbenevides", personList.get(0).getNick());
        assertEquals("Person's name should be 'Rafael Benevides'", "Rafael Benevides", personList.get(0).getName());
    }
    
    /**
     * Tests findPersonByNick and insures that the result is a Person object
     * (as opposed to a List<Person>).
     */
    @Test
    public void testFindByNick() {
        Person person = personQueryService.findPersonByNick("pmuir");
        assertEquals("Person's nick should be 'pmuir'", "pmuir", person.getNick());
        assertEquals("Person's name should be 'Pete Muir'", "Pete Muir", person.getName());
    }
}
