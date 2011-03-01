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

package org.jboss.seam.forge.resources.java;

import java.util.Collections;
import java.util.List;

import org.jboss.seam.forge.parser.java.Member;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.resources.VirtualResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaMemberResource<T extends Member<?, T>> extends VirtualResource<T>
{
   private final T member;

   public JavaMemberResource(final Resource<?> parent, final T member)
   {
      super(parent);
      this.member = member;
      setFlag(ResourceFlag.Leaf);
   }

   @Override
   public Resource<T> createFrom(final T file)
   {
      throw new RuntimeException("not implemented");
   }

   @Override
   public List<Resource<?>> listResources()
   {
      return Collections.emptyList();
   }

   @Override
   public T getUnderlyingResourceObject()
   {
      return member;
   }

   @Override
   public String getName()
   {
      return member.getName();
   }

   @Override
   public String toString()
   {
      return member.toString();
   }
}
