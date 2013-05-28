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
package org.jboss.as.quickstarts.picketlink.deltaspike.authorization;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.security.api.authorization.annotation.Secures;
import org.jboss.as.quickstarts.picketlink.deltaspike.authorization.annotations.Admin;
import org.jboss.as.quickstarts.picketlink.deltaspike.authorization.annotations.Employee;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;

/**
 * Defines the authorization logic for the @Employee and @Admin security binding types
 * 
 * @author Shane Bryzak
 * 
 */
@ApplicationScoped
public class CustomAuthorizer {

    /**
     * This method is used to check if classes and methods annotated with {@link Admin} can perform
     * the operation or not
     * 
     * @param identity The Identity bean, representing the currently authenticated user
     * @param identityManager The IdentityManager provides methods for checking a user's roles
     * @return true if the user can execute the method or class
     * @throws Exception
     */
    @Secures
    @Admin
    public boolean doAdminCheck(Identity identity, IdentityManager identityManager) throws Exception {
        return identityManager.hasRole(identity.getAgent(), identityManager.getRole("admin"));
    }

    /**
     * This method is used to check if classes and methods annotated with {@link Employee} can perform
     * the operation or not
     * 
     * @param identity The Identity bean, representing the currently authenticated user
     * @param identityManager The IdentityManager provides methods for checking a user's roles
     * @return true if the user can execute the method or class
     * @throws Exception
     */
    @Secures
    @Employee
    public boolean doEmployeeCheck(Identity identity, IdentityManager identityManager) throws Exception {
        return identityManager.hasRole(identity.getAgent(), identityManager.getRole("employee")) ||
               identityManager.hasRole(identity.getAgent(), identityManager.getRole("admin"));
    }

}
