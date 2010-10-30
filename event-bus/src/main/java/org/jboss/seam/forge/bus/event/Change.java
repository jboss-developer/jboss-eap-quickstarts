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

package org.jboss.seam.forge.bus.event;

/**
 *
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class Change<E>
{
   protected final Delta deltaType;
   protected final E changeType;
   protected final Payload<?> payload;

   public Change(Delta deltaType, E changeType, Payload<?> payload)
   {
      this.deltaType = deltaType;
      this.changeType = changeType;
      this.payload = payload;
   }

   public Delta getDeltaType()
   {
      return deltaType;
   }

   public E getChangeType()
   {
      return changeType;
   }

   public Payload<?> getPayload()
   {
      return payload;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof Change)) return false;

      Change change = (Change) o;

      if (changeType != null ? !changeType.equals(change.changeType) : change.changeType != null) return false;
      if (deltaType != change.deltaType) return false;
      if (payload != null ? !payload.equals(change.payload) : change.payload != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = deltaType != null ? deltaType.hashCode() : 0;
      result = 31 * result + (changeType != null ? changeType.hashCode() : 0);
      result = 31 * result + (payload != null ? payload.hashCode() : 0);
      return result;
   }
}
