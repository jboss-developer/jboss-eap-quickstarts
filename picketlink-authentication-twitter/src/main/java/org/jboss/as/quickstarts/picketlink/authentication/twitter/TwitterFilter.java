/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.authentication.twitter;

import org.picketlink.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for Twitter Login
 * @author Anil Saldhana
 * @since May 31, 2013
 */
@ApplicationScoped
@WebFilter("/*")
public class TwitterFilter implements Filter{
    @Inject
    private Instance<Identity> identityInstance;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            Identity identity = identityInstance.get();
            if(identity.isLoggedIn() == false){
                ThreadLocalUtils.currentRequest.set((HttpServletRequest) request);
                ThreadLocalUtils.currentResponse.set((HttpServletResponse) response);

                identity.login();
            }
            chain.doFilter(request,response);
        }finally{
            ThreadLocalUtils.currentRequest.set(null);
            ThreadLocalUtils.currentResponse.set(null);
        }
    }

    @Override
    public void destroy() {
    }
}