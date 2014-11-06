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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Provides an implementation of a query service that
 * uses the @QueryMethod annotation to provide a default
 * implementation for each query.
 *
 */
@RequestScoped
@QueryServiceBinding
public class QueryServicePartialBean implements InvocationHandler {
    
    @Inject
    private EntityManager em;
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        QueryMethod qMethod = method.getAnnotation(QueryMethod.class);
        if (qMethod == null) {
            throw new IllegalStateException("Method (" + method.getName() + ")called with no @QueryMethod annotation!");
        }
        
        String query = qMethod.value();
        boolean singleResult = qMethod.singleResult();
        
        Query q = em.createQuery(query);
        int idx = 1;
        // Attach all method parameters as query parameters
        if (args != null) {
            for (Object arg : args) {
                q.setParameter(idx, arg);
                idx++;
            }
        }
        if (singleResult) {
            // return a single result if this was requested in the annotation
            return q.getSingleResult();
        } else {
            // otherwise, return the full list
            return q.getResultList();
        }
    }
}
