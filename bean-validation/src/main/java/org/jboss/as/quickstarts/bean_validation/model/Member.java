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
package org.jboss.as.quickstarts.bean_validation.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/*@Entity declares the class as an entity (i.e. a persistent POJO class) */
@Entity
/*
 * @Table annotation specifies the primary table for the annotated entity. Additional tables may be specified using
 * SecondaryTable or SecondaryTables annotation.
 * 
 * @UniqueConstraint Specifies that a unique constraint is to be included in the generated DDL for a primary or secondary table.
 */
@Table(name = "MEMBER_BEAN_VALIDATION", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Member implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    /* @Id declares the identifier property of the entity. */
    @Id
    /*
     * @GeneratedValue Provides for the specification of generation strategies for the values of primary keys. The
     * GeneratedValue annotation may be applied to a primary key property or field of an entity or mapped superclass in
     * conjunction with the Id annotation.
     */
    @GeneratedValue
    private Long id;

    /* The value of the field or property must not be null. */
    @NotNull
    /*
     * The size of the field or property is evaluated and must match the specified boundaries.If the field or property is a
     * String, the size of the string is evaluated.If the field or property is a Collection, the size of the Collection is
     * evaluated.If the field or property is a Map, the size of the Map is evaluated.If the field or property is an array, the
     * size of the array is evaluated.Use one of the optional max or min elements to specify the boundaries.
     */
    @Size(min = 1, max = 25)
    /* The value of the field or property must match the regular expression defined in the regexp element. */
    @Pattern(regexp = "[A-Za-z ]*", message = "Must contain only letters and spaces")
    private String name;

    @NotNull
    /* Asserts that the annotated string, collection, map or array is not null or empty. */
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Size(min = 10, max = 12)
    /*
     * The value of the field or property must be a number within a specified range. The integer element specifies the maximum
     * integral digits for the number, and the fraction element specifies the maximum fractional digits for the number.
     */
    @Digits(fraction = 0, integer = 12)
    /*
     * @Column is used to specify a mapped column for a persistent property or field. If no Column annotation is specified, the
     * default values are applied.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @NotEmpty
    // The value of the field or property must be a date in the past.
    @Past
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @NotNull
    @NotEmpty
    // The value of the field or property must be a date in the future.
    @Future
    @Column(name = "event_date")
    private Date eventDate;

    @NotNull
    // The value of the field or property must be an integer value lower than or equal to the number in the value element.
    @Max(10)
    @Column(name = "max_quantity")
    private int maxQuantity;

    @NotNull
    // The value of the field or property must be an integer value greater than or equal to the number in the value element.
    @Min(10)
    @Column(name = "min_quantity")
    private int minQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }
}
