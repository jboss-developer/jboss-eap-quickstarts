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

import javax.ejb.Stateless;

import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;

/**
 * Bean implementing access to the SingletonBean
 *
 * @author Radoslav Husar
 * @version February 2013
 */
@Stateless
public class SingletonBeanAccessProviderBean implements SingletonBeanAccessProvider {
    private static final Logger LOGGER = Logger.getLogger(SingletonBeanAccessProviderBean.class);

    public String getInvocationInformation() {
        LOGGER.info("invoking getInvocationInformation()");

        ServiceController<?> service = CurrentServiceContainer.getServiceContainer().getService(ClusterwideSingletonBeanService.SERVICE_NAME);

        if (service != null) {
            ClusterwideSingleton singletonBean = (ClusterwideSingleton) service.getValue();
            return singletonBean.getInvocationInformation();
        } else {
            return "Service " + ClusterwideSingletonBeanService.SERVICE_NAME + " was not found!";

            /**
             * Typically you will want to throw an exception here. For the purposes of the demo, lets output some
             * readable form of the error state.
             */
            //throw new IllegalStateException("Service " + ClusterwideSingletonBeanService.SERVICE_NAME + " was not found!");
        }
    }
}
