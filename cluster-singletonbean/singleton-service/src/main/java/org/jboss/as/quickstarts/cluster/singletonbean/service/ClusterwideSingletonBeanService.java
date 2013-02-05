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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;


/**
 * Service to lookup a clusterwide singleton bean.
 *
 * @author Radoslav Husar
 * @version February 2013
 */
public class ClusterwideSingletonBeanService implements Service<ClusterwideSingleton> {

    private static final Logger LOGGER = Logger.getLogger(ClusterwideSingletonBeanService.class);

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("quickstart", "ha", "singleton", "bean");

    /**
     * Flag whether the service is started.
     */
    private final AtomicBoolean started = new AtomicBoolean(false);

    /**
     * Current node name will be used to set the original node name that constructed the singleton bean.
     */
    private String nodeName;

    private ClusterwideSingleton singletonInstance = null;

    public void setSingletonInstance(ClusterwideSingleton singletonInstance) {
        this.singletonInstance = singletonInstance;
    }

    final InjectedValue<ServerEnvironment> env = new InjectedValue<ServerEnvironment>();

    public ClusterwideSingletonBeanService() {
        LOGGER.info("Service is instantiated.");
    }

    public void start(StartContext context) throws StartException {
        if (!started.compareAndSet(false, true)) {
            throw new StartException("The service is already started!");
        }

        LOGGER.info("Service " + this.getClass().getName() + " starting.");

        this.nodeName = this.env.getValue().getNodeName();

        if (singletonInstance != null) return;

        try {
            InitialContext ic = new InitialContext();
            singletonInstance = ((ClusterwideSingleton) ic.lookup("global/jboss-as-cluster-singletonbean-service/ClusterwideSingletonBean!org.jboss.as.quickstarts.cluster.singletonbean.service.ClusterwideSingleton"));
            singletonInstance.initialize(nodeName);
        } catch (NamingException e) {
            throw new StartException("Could not initialize Singleton bean!", e);
        }
    }

    public ClusterwideSingleton getValue() throws IllegalStateException, IllegalArgumentException {
        if (!started.get()) {
            throw new IllegalStateException("Service " + this.getClass().getName() + " is not started yet!");
        }
        return singletonInstance;
    }

    public void stop(StopContext context) {
        if (!started.compareAndSet(true, false)) {
            LOGGER.warn("Service " + this.getClass().getName() + " is not active!");
        } else {
            LOGGER.info("Service " + this.getClass().getName() + " stopping.");
        }
    }
}
