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
package org.jboss.as.quickstart.deltaspike.partialbeanadvanced.queryservice;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.jboss.as.quickstart.deltaspike.partialbeanadvanced.model.Person;

/**
 * Sample query service for the Person Entity. 
 *
 * This provides a number of query methods, that are implemented by the 
 * QueryServicePartialBean. The entire query is configured
 * via the @QueryMethod annotation.
 */
@RequestScoped
@QueryServiceBinding
public interface PersonQueryService {
    /**
     * Returns all person entries in the Person table
     */
    @QueryMethod("select p from Person p")
    public List<Person> findAll();
    
    /**
     * Returns all person entries in the Person table, sorted
     * by person.name
     */
    @QueryMethod("select p from Person p order by p.name")
    public List<Person> findAllSortedByName();
    
    /**
     * Returns the person entry with the given nick
     */
    @QueryMethod(value = "select p from Person p where p.nick = ?", singleResult=true)
    public Person findPersonByNick(String nick);
    
    /**
     * Returns all person entries in the Person table, filtered
     * by person.name
     */
    @QueryMethod("select p from Person p where p.name = ?")
    public List<Person> findByName(String name);
}
