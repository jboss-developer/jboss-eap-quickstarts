/*
 * JBoss, by Red Hat.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

package org.jboss.seam.forge.shell.test.project.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.resources.builtin.JavaResource;
import org.jboss.seam.forge.shell.project.resources.ResourceProducer;
import org.jboss.seam.forge.shell.project.resources.ResourceProducerExtension;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
//@RunWith(Arquillian.class)
public class ResourceProducerExtensionTest
      //extends AbstractShellTest
{

   @Test
   public void testNothing()
   {
   }

//   @Deployment
//   public static JavaArchive getDeployment()
//   {
//      return AbstractShellTest.getDeployment()
//            .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension")
//            .addClass(ResourceProducerExtension.class)
//            .addClass(ResourceProducer.class)
//            .addClass(MockResourceInjectionPlugin.class);
//   }
//
//   @Inject
//   private Event<MockEvent> event;
//
//   @Inject
//   private MockResourceInjectionPlugin plugin;
//   @Test
//   public void testGenericResourceInjection() throws Exception
//   {
//      Resource<?> resource = plugin.getR();
//      assertNotNull(resource);
//   }
//
//  @Test
//   public void testSpecificResourceInjection() throws Exception
//   {
//      DirectoryResource resource = plugin.getD();
//      assertNotNull(resource);
//   }
//
//   @Test
//   public void testSpecificResourceInjectionNullIfIncorrectType() throws Exception
//   {
//      JavaResource resource = plugin.getJ();
//      assertNull(resource);
//   }
//
//   @Test
//   public void testMethodParameterInjection() throws Exception
//   {
//      event.fire(new MockEvent());
//      Resource<?> resource = plugin.getObservedResource();
//      assertNotNull(resource);
//   }
}
