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
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.picketlink.idm.jpa.annotations.CreationDate;
import org.picketlink.idm.jpa.annotations.Discriminator;
import org.picketlink.idm.jpa.annotations.Email;
import org.picketlink.idm.jpa.annotations.Enabled;
import org.picketlink.idm.jpa.annotations.ExpiryDate;
import org.picketlink.idm.jpa.annotations.FirstName;
import org.picketlink.idm.jpa.annotations.GroupPath;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.IdentityName;
import org.picketlink.idm.jpa.annotations.IdentityPartition;
import org.picketlink.idm.jpa.annotations.IdentityType;
import org.picketlink.idm.jpa.annotations.LastName;
import org.picketlink.idm.jpa.annotations.LoginName;
import org.picketlink.idm.jpa.annotations.Parent;

/**
 * <p>JPA {@link Entity} that maps IdentityType instances.</p>
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@IdentityType
@Entity
public class IdentityObject implements Serializable {

    private static final long serialVersionUID = -9155861474157098664L;

    @Discriminator
    private String discriminator;

    @IdentityPartition
    @ManyToOne
    private PartitionObject partition;

    @Identifier
    @Id
    private String id;

    @LoginName
    private String loginName;

    @IdentityName
    private String name;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @Email
    private String email;

    @Enabled
    private boolean enabled;

    @CreationDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ExpiryDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Parent
    @ManyToOne
    private IdentityObject parent;

    @GroupPath
    private String groupPath;

    public String getDiscriminator() {
        return this.discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PartitionObject getPartition() {
        return partition;
    }

    public void setPartition(PartitionObject partition) {
        this.partition = partition;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public IdentityObject getParent() {
        return this.parent;
    }

    public void setParent(IdentityObject parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!getClass().isInstance(obj)) {
            return false;
        }

        IdentityObject other = (IdentityObject) obj;

        return getId() != null && other.getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
