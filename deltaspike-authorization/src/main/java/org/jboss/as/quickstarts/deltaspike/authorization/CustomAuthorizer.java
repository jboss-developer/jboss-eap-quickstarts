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
package org.jboss.as.quickstarts.deltaspike.authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;

import org.apache.deltaspike.security.api.authorization.annotation.Secures;

/**
 * This Authorizer class implements behavior for our security binding types. This class is simply a CDI bean which declares
 * a @Secures method, qualified with the security binding annotation.
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@ApplicationScoped
public class CustomAuthorizer {

    @Inject
    private FacesContext facesContext;

    /**
     * This method is used to check if classes and methods annotated with {@link AdminAllowed} can perform
     * the operation or not
     * 
     * @param invocationContext
     * @param manager
     * @return true if the user can execute the method or class
     * @throws Exception
     */
    @Secures
    @AdminAllowed
    public boolean doAdminCheck(InvocationContext invocationContext, BeanManager manager) throws Exception {
        return facesContext.getExternalContext().isUserInRole("admin");
    }

    /**
     * This method is used to check if classes and methods annotated with {@link EmployeeAllowed} can perform
     * the operation or not
     * 
     * @param invocationContext
     * @param manager
     * @return true if the user can execute the method or class
     * @throws Exception
     */
    @Secures
    @EmployeeAllowed
    public boolean doGuestCheck(InvocationContext invocationContext, BeanManager manager) throws Exception {
        return facesContext.getExternalContext().isUserInRole("guest");
    }

}
