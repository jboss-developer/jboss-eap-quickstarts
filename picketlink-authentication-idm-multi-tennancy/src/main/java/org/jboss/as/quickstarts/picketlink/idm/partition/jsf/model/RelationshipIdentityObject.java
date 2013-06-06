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
package org.jboss.as.quickstarts.picketlink.idm.partition.jsf.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.picketlink.idm.jpa.annotations.Identity;
import org.picketlink.idm.jpa.annotations.Parent;
import org.picketlink.idm.jpa.annotations.RelationshipDescriptor;
import org.picketlink.idm.jpa.annotations.RelationshipIdentity;

/**
 * <p>
 * JPA {@link Entity} to store the IdentityType instances associated with a specific {@link RelationshipObject}. This
 * class should be used when the JPA store is being used to store IdentityType instances, forcing the their existence on
 * the database.
 * </p>
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@RelationshipIdentity
@Entity
public class RelationshipIdentityObject implements Serializable {

    private static final long serialVersionUID = 8957185191684867238L;

    @Id
    @GeneratedValue
    private Long id;

    @RelationshipDescriptor
    private String descriptor;

    @Identity
    @ManyToOne
    private IdentityObject identityObject;

    @Parent
    @ManyToOne
    private RelationshipObject relationshipObject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdentityObject getIdentityObject() {
        return identityObject;
    }

    public void setIdentityObject(IdentityObject identityObject) {
        this.identityObject = identityObject;
    }

    public RelationshipObject getRelationshipObject() {
        return relationshipObject;
    }

    public void setRelationshipObject(RelationshipObject relationshipObject) {
        this.relationshipObject = relationshipObject;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!getClass().isInstance(obj)) {
            return false;
        }

        RelationshipIdentityObject other = (RelationshipIdentityObject) obj;

        return getId() != null && other.getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
