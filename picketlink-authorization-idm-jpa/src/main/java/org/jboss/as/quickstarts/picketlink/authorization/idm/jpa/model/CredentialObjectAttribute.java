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
package org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.picketlink.idm.jpa.annotations.AttributeName;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.CredentialAttribute;
import org.picketlink.idm.jpa.annotations.Parent;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@CredentialAttribute
@Entity
public class CredentialObjectAttribute implements Serializable {

    private static final long serialVersionUID = 1500299957446203938L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    @Parent
    private CredentialObject credentialObject;

    @AttributeName
    private String name;

    @AttributeValue
    @Column (length=1024)
    private String value;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CredentialObject getCredentialObject() {
        return this.credentialObject;
    }

    public void setCredentialObject(CredentialObject credentialObject) {
        this.credentialObject = credentialObject;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!getClass().isInstance(obj)) {
            return false;
        }

        CredentialObjectAttribute other = (CredentialObjectAttribute) obj;

        return getId() != null && other.getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
