/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.quickstarts.invocationhandler;

import java.util.logging.Logger;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * @author <a href="mailto:jsightle@redhat.com">Jess Sightler</a>
 *
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        WeldContainer weld = new Weld().initialize();

        HelloWorldBean helloWorldBean  = weld.instance().select(HelloWorldBean.class).get();

        String result = helloWorldBean.sayHello("JBoss Client");
        log.info("Hello call result: " + result);
    }

}
