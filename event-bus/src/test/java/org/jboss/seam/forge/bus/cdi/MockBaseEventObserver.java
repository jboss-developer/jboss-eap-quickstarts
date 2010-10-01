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

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import org.jboss.seam.forge.bus.event.BaseEvent;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
public class MockBaseEventObserver
{
   private boolean observedRemoved = false;
   private boolean observedRemoved2 = false;
   private boolean observedNormal = false;

   public void managedObserver(@Observes final BaseEvent event)
   {
      observedRemoved = true;
   }

   public void managedObserver2(@Observes final BaseEvent event)
   {
      observedRemoved2 = true;
   }

   public void activeObserver(@Observes final MockNormalEvent event)
   {
      observedNormal = true;
   }

   public boolean hasObservedRemoved()
   {
      return observedRemoved;
   }

   public void setObservedRemoved(final boolean observed)
   {
      this.observedRemoved = observed;
   }

   public boolean hasObservedNormal()
   {
      return observedNormal;
   }

   public void setObservedNormal(final boolean observedNormal)
   {
      this.observedNormal = observedNormal;
   }

   public boolean hasObservedRemoved2()
   {
      return observedRemoved2;
   }

   public void setObservedRemoved2(final boolean observedRemoved2)
   {
      this.observedRemoved2 = observedRemoved2;
   }

}
