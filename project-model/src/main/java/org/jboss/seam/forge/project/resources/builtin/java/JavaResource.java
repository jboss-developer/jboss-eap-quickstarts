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

package org.jboss.seam.forge.project.resources.builtin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.Member;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.ResourceException;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ResourceHandles("*.java")
public class JavaResource extends FileResource<JavaResource>
{
   private volatile JavaSource<?> source;

   @Inject
   public JavaResource(final ResourceFactory factory)
   {
      super(factory, null);
   }

   public JavaResource(final ResourceFactory factory, final File file)
   {
      super(factory, file);
      setFlag(ResourceFlag.ProjectSourceFile);
   }

   @Override
   public Resource<?> getChild(final String name)
   {
      List<Resource<?>> children = listResources();
      List<Resource<?>> subset = new ArrayList<Resource<?>>();

      for (Resource<?> child : children)
      {
         if ((name != null) && (child instanceof JavaMemberResource<?>))
         {
            String childName = child.getName();
            if (((Member<?, ?>) child.getUnderlyingResourceObject()).getName().equals(name.trim())
                     || childName.equals(name))
            {
               subset.add(child);
            }
         }
      }

      if (subset.size() == 1)
      {
         return subset.get(0);
      }
      else if (subset.size() > 1)
      {
         throw new ResourceException("Ambiguous name [" + name + "], full type signature required");
      }
      else
      {
         return null;
      }
   }

   @Override
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public synchronized List<Resource<?>> listResources()
   {
      try
      {
         lazyInitialize();
      }
      catch (FileNotFoundException e)
      {
         return Collections.emptyList();
      }

      List<Resource<?>> list = new LinkedList<Resource<?>>();

      for (Member<?, ?> member : source.getMembers())
      {
         list.add(new JavaMemberResource(this, member));
      }

      return list;
   }

   public JavaResource setContents(final JavaSource<?> source)
   {
      setContents(source.toString());
      return this;
   }

   private void lazyInitialize() throws FileNotFoundException
   {
      if (source == null)
      {
         source = JavaParser.parse(file);
      }
   }

   /**
    * Attempts to perform cast automatically. This can lead to problems.
    */
   public JavaSource<?> getJavaSource() throws FileNotFoundException
   {
      lazyInitialize();
      return source;
   }

   @Override
   public JavaResource createFrom(final File file)
   {
      return new JavaResource(resourceFactory, file);
   }

   @Override
   public String toString()
   {
      try
      {
         return getJavaSource().getQualifiedName();
      }
      catch (FileNotFoundException e)
      {
         throw new ResourceException(e);
      }
      catch (Exception e)
      {
         return getName();
      }
   }
}
