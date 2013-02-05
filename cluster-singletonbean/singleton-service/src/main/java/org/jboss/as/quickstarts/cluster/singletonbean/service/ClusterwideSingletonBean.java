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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;

import org.jboss.ejb3.annotation.Clustered;
import org.jboss.logging.Logger;

/**
 * @author Radoslav Husar
 * @version February 2013
 */
@Stateful
@Clustered
public class ClusterwideSingletonBean implements ClusterwideSingleton {

    private static Logger LOGGER = Logger.getLogger(ClusterwideSingletonBean.class);

    /**
     * Incremented number of invocations. This will demonstrate that failover works.
     */
    private int numberOfInvocations = 0;

    /**
     * Specifies which node initially created the Singleton bean. This should help developers better understand
     * what happened at failover.
     */
    private String originalCreatorNodeName;

    @PostConstruct
    public void construct() {
        LOGGER.info("Bean is constructed.");
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("Bean is being destroyed.");
    }

    @Override
    public void initialize(String nodeName) {
        originalCreatorNodeName = nodeName;

        LOGGER.info("Bean is initialized.");
    }

    @Override
    public String getInvocationInformation() {

        numberOfInvocations++;

        // Get some information together
        StringBuilder out = new StringBuilder()
                .append("You are accessing: ").append(this.toString()).append("\n")
                .append("Number of invocations: ").append(this.numberOfInvocations).append("\n")
                .append("Originally created on node: ").append(this.originalCreatorNodeName);

        return out.toString();
    }
}
