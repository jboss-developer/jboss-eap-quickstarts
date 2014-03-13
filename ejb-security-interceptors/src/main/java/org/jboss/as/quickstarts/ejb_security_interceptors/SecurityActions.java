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
package org.jboss.as.quickstarts.ejb_security_interceptors;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;

import javax.security.auth.Subject;

import org.jboss.as.security.api.ConnectionSecurityContext;
import org.jboss.as.security.api.ContextStateCache;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextFactory;

/**
 * Security actions for this package only.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
final class SecurityActions {

    private SecurityActions() {
    }

    /*
     * ConnectionSecurityContext Actions
     */

    static Collection<Principal> getConnectionPrincipals() {
        return connectionSecurityContextActions().getConnectionPrincipals();
    }

    static ContextStateCache pushIdentity(final Principal principal, final Object credential) throws Exception {
        return connectionSecurityContextActions().pushIdentity(principal, credential);
    }

    static void popIdentity(final ContextStateCache stateCache) {
        connectionSecurityContextActions().popIdentity(stateCache);
    }

    private static ConnectionSecurityContextActions connectionSecurityContextActions() {
        return System.getSecurityManager() == null ? ConnectionSecurityContextActions.NON_PRIVILEGED : ConnectionSecurityContextActions.PRIVILEGED;
    }

    private interface ConnectionSecurityContextActions {

        Collection<Principal> getConnectionPrincipals();

        ContextStateCache pushIdentity(final Principal principal, final Object credential) throws Exception;

        void popIdentity(final ContextStateCache stateCache);

        ConnectionSecurityContextActions NON_PRIVILEGED = new ConnectionSecurityContextActions() {

            public Collection<Principal> getConnectionPrincipals() {
                return ConnectionSecurityContext.getConnectionPrincipals();
            }

            @Override
            public ContextStateCache pushIdentity(final Principal principal, final Object credential) throws Exception {
                return ConnectionSecurityContext.pushIdentity(principal, credential);
            }

            @Override
            public void popIdentity(ContextStateCache stateCache) {
                ConnectionSecurityContext.popIdentity(stateCache);
            }
        };

        ConnectionSecurityContextActions PRIVILEGED = new ConnectionSecurityContextActions() {

            PrivilegedAction<Collection<Principal>> GET_CONNECTION_PRINCIPALS_ACTION = new PrivilegedAction<Collection<Principal>>() {

                @Override
                public Collection<Principal> run() {
                    return NON_PRIVILEGED.getConnectionPrincipals();
                }
            };

            public Collection<Principal> getConnectionPrincipals() {
                return AccessController.doPrivileged(GET_CONNECTION_PRINCIPALS_ACTION);
            }

            @Override
            public ContextStateCache pushIdentity(final Principal principal, final Object credential) throws Exception {
                try {
                    return AccessController.doPrivileged(new PrivilegedExceptionAction<ContextStateCache>() {

                        @Override
                        public ContextStateCache run() throws Exception {
                            return NON_PRIVILEGED.pushIdentity(principal, credential);
                        }
                    });
                } catch (PrivilegedActionException e) {
                    throw e.getException();
                }
            }

            @Override
            public void popIdentity(final ContextStateCache stateCache) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {

                    @Override
                    public Void run() {
                        NON_PRIVILEGED.popIdentity(stateCache);
                        return null;
                    }
                });

            }

        };

    }

    /*
     * SecurityContext Actions
     */

    static void securityContextSet(final SecurityContext context) {
        securityContextActions().setSecurityContext(context);
    }

    static void securityContextClear() {
        securityContextActions().clear();
    }

    static Principal securityContextGetPrincipal() {
        return securityContextActions().getPrincipal();
    }

    static void securityContextSetPrincpal(final Principal principal) {
        securityContextActions().setPrincipal(principal);
    }

    /**
     * @return The SecurityContext previously set if any.
     */
    static SecurityContext securityContextSetPrincipalInfo(final Principal principal, final OuterUserCredential credential)
        throws Exception {
        return securityContextActions().setPrincipalInfo(principal, credential);
    }

    private static SecurityContextActions securityContextActions() {
        return System.getSecurityManager() == null ? SecurityContextActions.NON_PRIVILEGED : SecurityContextActions.PRIVILEGED;
    }

    private interface SecurityContextActions {

        void setSecurityContext(final SecurityContext context);

        void clear();

        Principal getPrincipal();

        void setPrincipal(final Principal principal);

        SecurityContext setPrincipalInfo(final Principal principal, final OuterUserCredential credential) throws Exception;

        SecurityContextActions NON_PRIVILEGED = new SecurityContextActions() {

            public void setSecurityContext(SecurityContext context) {
                SecurityContextAssociation.setSecurityContext(context);
            }

            public void clear() {
                SecurityContextAssociation.clearSecurityContext();
            }

            public Principal getPrincipal() {
                return SecurityContextAssociation.getPrincipal();
            }

            public void setPrincipal(final Principal principal) {
                SecurityContextAssociation.setPrincipal(principal);
            }

            public SecurityContext setPrincipalInfo(Principal principal, OuterUserCredential credential) throws Exception {
                SecurityContext currentContext = SecurityContextAssociation.getSecurityContext();

                SecurityContext nextContext = SecurityContextFactory.createSecurityContext(principal, credential,
                    new Subject(), "USER_DELEGATION");
                SecurityContextAssociation.setSecurityContext(nextContext);

                return currentContext;
            }

        };

        SecurityContextActions PRIVILEGED = new SecurityContextActions() {

            PrivilegedAction<Principal> GET_PRINCIPAL_ACTION = new PrivilegedAction<Principal>() {

                public Principal run() {
                    return NON_PRIVILEGED.getPrincipal();
                }
            };

            PrivilegedAction<Void> CLEAR_ACTION = new PrivilegedAction<Void>() {

                public Void run() {
                    NON_PRIVILEGED.clear();
                    return null;
                }
            };

            public void setSecurityContext(final SecurityContext context) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {

                    public Void run() {
                        NON_PRIVILEGED.setSecurityContext(context);
                        return null;
                    }
                });
            }

            public void clear() {
                AccessController.doPrivileged(CLEAR_ACTION);
            }

            public Principal getPrincipal() {
                return AccessController.doPrivileged(GET_PRINCIPAL_ACTION);
            }

            public void setPrincipal(final Principal principal) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {

                    public Void run() {
                        NON_PRIVILEGED.setPrincipal(principal);
                        return null;
                    }
                });
            }

            public SecurityContext setPrincipalInfo(final Principal principal, final OuterUserCredential credential)
                throws Exception {
                try {
                    return AccessController.doPrivileged(new PrivilegedExceptionAction<SecurityContext>() {

                        public SecurityContext run() throws Exception {
                            return NON_PRIVILEGED.setPrincipalInfo(principal, credential);
                        }
                    });
                } catch (PrivilegedActionException e) {
                    throw e.getException();
                }
            }

        };

    }

    /*
     * ClassLoader Actions
     */

    static ClassLoader getContextClassLoader() {
        return (System.getSecurityManager() == null ? ContextClassLoaderAction.NON_PRIVILEGED
            : ContextClassLoaderAction.PRIVILEGED).getContextClassLoader();
    }

    private interface ContextClassLoaderAction {

        ClassLoader getContextClassLoader();

        ContextClassLoaderAction NON_PRIVILEGED = new ContextClassLoaderAction() {

            public ClassLoader getContextClassLoader() {
                return Thread.currentThread().getContextClassLoader();
            }
        };

        ContextClassLoaderAction PRIVILEGED = new ContextClassLoaderAction() {

            private PrivilegedAction<ClassLoader> GET_CONTEXT_CLASS_LOADER = new PrivilegedAction<ClassLoader>() {

                public ClassLoader run() {
                    return NON_PRIVILEGED.getContextClassLoader();
                }
            };

            public ClassLoader getContextClassLoader() {
                return AccessController.doPrivileged(GET_CONTEXT_CLASS_LOADER);
            }
        };
    }
}
