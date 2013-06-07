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
package org.jboss.as.quickstarts.picketlink.authorization.rest.rbac;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import org.apache.deltaspike.security.api.authorization.annotation.Secures;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Agent;
import org.picketlink.idm.model.Role;

/**
 * <p>This class centralizes all authorization services for this application.</p>
 * 
 * @author pedroigor
 * 
 */
@ApplicationScoped
public class AuthorizationManager {

    @Inject
    private Instance<Identity> identityInstance;

    @Inject
    private IdentityManager identityManager;

    /**
     * <p>
     *  This authorization method provides the validation logic for resources annotated with the security annotation {@link DeclareRoles}.
     * </p>
     * <p>
     *  Note that this method is also annotated with {@link Secures}, which is an annotation from Apache DeltaSpike.
     *  This annotation tells the @{link SecurityInterceptor} that this method must be called before the execution of
     *  methods annotated with {@checkDeclaredRoles} in order to perform authorization checks.
     * </p>
     * 
     * @param invocationContext
     * @param manager
     * @return true if the user can execute the method or class
     * @throws Exception
     */
    @Secures
    @DeclareRoles
    public boolean checkDeclaredRoles(InvocationContext invocationContext, BeanManager manager) throws Exception {
        // administrators can access everything
        if (hasRole(ApplicationRole.ADMINISTRATOR.name())) {
            return true;
        }

        Object targetBean = invocationContext.getTarget();

        DeclareRoles declareRoles = targetBean.getClass().getAnnotation(DeclareRoles.class);

        if (declareRoles == null) {
            declareRoles = invocationContext.getMethod().getAnnotation(DeclareRoles.class);
        }

        ApplicationRole[] requiredRoles = declareRoles.value();

        if (requiredRoles.length == 0) {
            throw new IllegalArgumentException("@DeclaredRoles does not define any role.");
        }

        for (ApplicationRole requiredRole: requiredRoles) {
            if (hasRole(requiredRole.name())) {
                return true;
            }
        }

        return false;
    }

    public boolean hasRole(String roleName) {
        Agent agent = getIdentity().getAgent();
        Role role = this.identityManager.getRole(roleName);

        return this.identityManager.hasRole(agent, role);
    }

    private Identity getIdentity() {
        return this.identityInstance.get();
    }
}
