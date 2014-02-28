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
package org.jboss.as.quickstarts.bean_validation_customConstraint;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PERSON_BEAN_VALIDATION")
public class MyPerson implements Serializable {

    @Id
    @GeneratedValue
    private static final long serialVersionUID = 1L;

    /* Asserts that the annotated string, collection, map or array is not null or empty. */
    @NotNull
    /*
     * The size of the field or property is evaluated and must match the specified boundaries.If the field or property is a
     * String, the size of the string is evaluated.If the field or property is a Collection, the size of the Collection is
     * evaluated.If the field or property is a Map, the size of the Map is evaluated.If the field or property is an array, the
     * size of the array is evaluated.Use one of the optional max or min elements to specify the boundaries.
     */
    @Size(min = 4)
    private String firstName;

    @NotNull
    @Size(min = 4)
    private String lastName;

    // Custom Constraint @Address for bean validation
    @Address
    private MyAddress address;

    public MyPerson() {

    }

    public MyPerson(String firstName, String lastName, MyAddress address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MyAddress getAddress() {
        return address;
    }

    public void setAddress(MyAddress address) {
        this.address = address;
    }

}