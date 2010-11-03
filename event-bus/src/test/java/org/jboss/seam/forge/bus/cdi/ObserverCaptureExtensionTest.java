/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.seam.forge.bus.cdi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.bus.event.BaseEvent;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class ObserverCaptureExtensionTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
               .addClass(ObserverCaptureExtension.class)
               .addClass(MockBaseEventObserver.class)
               .addClass(BaseEvent.class)
               .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension")
               .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"));
   }

   BaseEvent event = new BaseEvent()
   {
   };

   @Inject
   private BeanManager manager;

   @Inject
   private MockBaseEventObserver observer;

   @Inject
   private ObserverCaptureExtension oce;

   @Before
   public void reset()
   {
      observer.setObservedNormal(false);
      observer.setObservedRemoved(false);
      observer.setObservedRemoved2(false);
   }

   @Test
   public void testRemovedObserversDoNotObserve() throws Exception
   {
      assertFalse(observer.hasObservedRemoved());
      manager.fireEvent(event, new Annotation[] {});
      assertFalse(observer.hasObservedRemoved());
   }

   @Test
   public void testRemovedObserversInvokeViaQualifiedEvent() throws Exception
   {
      assertFalse(observer.hasObservedRemoved());
      List<BusManaged> qualifiers = oce.getEventQualifiers(event.getClass());
      BusManaged busManaged = qualifiers.get(0);
      manager.fireEvent(event, new Annotation[] { busManaged });
      assertTrue(observer.hasObservedRemoved());
   }

   @Test
   public void testRemovedObserversInvokeUniquelyViaQualifiedEvent() throws Exception
   {
      assertFalse(observer.hasObservedRemoved());
      List<BusManaged> qualifiers = oce.getEventQualifiers(event.getClass());
      BusManaged busManaged = qualifiers.get(0);
      manager.fireEvent(event, new Annotation[] { busManaged });
      assertTrue(observer.hasObservedRemoved());

      assertFalse(observer.hasObservedRemoved2());
   }

   @Test
   public void testNormalObserversContinueToObserve() throws Exception
   {
      assertFalse(observer.hasObservedNormal());
      manager.fireEvent(new MockNormalEvent(), new Annotation[] {});
      assertTrue(observer.hasObservedNormal());
   }
}
