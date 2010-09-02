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

package org.jboss.seam.sidekick.bus.event;

import java.util.*;

/**
 * A ChangeSet is a representation of all of the changes that are pending against the project structure.</p>
 *
 * @author Mike Brock <cbrock@redhat.com>
 */
public class ChangeSet
{
   private AbstractSequentialList<Change> changes;
   private Set<Payload<?>> payloads;

   public ChangeSet()
   {
      changes = new LinkedList<Change>();
      payloads = new LinkedHashSet<Payload<?>>();
   }

   /**
    * Adds a change to the changeset.
    * @param change
    */
   public void addChange(Change change)
   {
      payloads.add(change.getPayload());
      changes.add(change);
   }

   /**
    * Returns an ordered list of all the {@link org.jboss.seam.sidekick.bus.event.Change} objects associatd with this changeset}.
    * @return A list of changes.
    */
   public List<Change> getChanges()
   {
      return Collections.unmodifiableList(changes);
   }

   /**
    * Return a set of {@link org.jboss.seam.sidekick.bus.event.Payload} associated with this changeset.
    * @return A list of payloads.
    */
   public Set<Payload<?>> getPayloads()
   {
      return Collections.unmodifiableSet(payloads);
   }

   /**
    * Produces a differential changeset
    * @param change
    * @return
    */
   public ChangeSet diff(final ChangeSet change)
   {
      final ChangeSet newChangeSet = new ChangeSet();

      for (Change c : changes)
      {
         if (!change.changes.contains(c))
         {
            newChangeSet.addChange(c);
         }
      }

      return newChangeSet;
   }
}
