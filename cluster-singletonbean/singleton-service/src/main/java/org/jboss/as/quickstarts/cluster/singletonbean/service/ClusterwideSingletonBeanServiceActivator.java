/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.cluster.singletonbean.service;

import org.jboss.as.clustering.singleton.SingletonService;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.logging.Logger;
import org.jboss.msc.service.DelegatingServiceContainer;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;

/**
 * Service activator that installs the ClusterwideSingletonBeanService as a clustered SingletonService
 * during deployment.
 *
 * @author Paul Ferraro
 * @author Radoslav Husar
 * @version February 2013
 */
public class ClusterwideSingletonBeanServiceActivator implements ServiceActivator {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    @Override
    public void activate(ServiceActivatorContext context) {
        LOGGER.info("Service is being activated.");

        final ClusterwideSingletonBeanService service = new ClusterwideSingletonBeanService();

        final SingletonService<ClusterwideSingleton> singletonService = new SingletonService<ClusterwideSingleton>(service, ClusterwideSingletonBeanService.SERVICE_NAME);

        /*
         * We can pass a chain of election policies to the singletonService, for example to tell JGroups to prefer running the singletonService on a node with a
         * particular name
         */
        // singletonService.setElectionPolicy(new PreferredSingletonElectionPolicy(new SimpleSingletonElectionPolicy(), new NamePreference("node2/cluster")));

        singletonService
                .build(new DelegatingServiceContainer(context.getServiceTarget(), context.getServiceRegistry()))
                .addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, service.env)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .addListener(new ServiceListener<ClusterwideSingleton>() {
                    @Override
                    public void transition(ServiceController<? extends ClusterwideSingleton> controller, ServiceController.Transition transition) {
                        LOGGER.debug("New transition: " + transition);

                        if (transition.getAfter().equals(ServiceController.Substate.UP)) {
                            if (!singletonService.isMaster()) {
                                ServiceController<?> remoteService = CurrentServiceContainer.getServiceContainer().getService(ClusterwideSingletonBeanService.SERVICE_NAME);
                                ClusterwideSingleton singletonBean = (ClusterwideSingleton) remoteService.getValue();

                                service.setSingletonInstance(singletonBean);

                                LOGGER.info("This is a slave node. Getting a singletonService bean proxy from the master. Got: " + singletonBean);
                            }
                        }
                    }

                    @Override
                    public void listenerAdded(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void serviceRemoveRequested(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void serviceRemoveRequestCleared(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void dependencyFailed(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void dependencyFailureCleared(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void immediateDependencyUnavailable(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void immediateDependencyAvailable(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void transitiveDependencyUnavailable(ServiceController<? extends ClusterwideSingleton> controller) {
                    }

                    @Override
                    public void transitiveDependencyAvailable(ServiceController<? extends ClusterwideSingleton> controller) {
                    }
                })
                .install()
        ;

    }
}
