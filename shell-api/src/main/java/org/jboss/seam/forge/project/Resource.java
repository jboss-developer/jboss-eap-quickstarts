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

package org.jboss.seam.forge.project;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.jboss.seam.forge.resources.ResourceFilter;

/**
 * A Resource is an abstraction on top of usable items within a Forge project. For instance, files, source code, etc.
 * Like a simplified virtual file system, a Resource is represented hierarchically with a parent and children. This
 * allows plugins to say, direct access to project elements within a consistent API from files to class members. </br>
 * However, Resource instances should be treated as representative query result objects. A modification to an instance
 * variable in a resource will not be persisted. Rather than thinking of the Resource object as meta-data (which it is
 * not), it is better conceptualized as a wrapper or "view" of an underlying resource such as a File. For this reason,
 * custom Resource types should never implement any sort of static cache and should preferably lazily initialize their
 * data.
 * 
 * @author Mike Brock
 */
public interface Resource<T>
{

   /*
    * TODO Add a ResourceFilter API/Utility
    * 
    * Resource.listResources(ResourceFilter filter) <T> List<T> Filter.apply(ResourceFilter<T> filter, List<Resource<?>>
    * resources) List<Resource<?>> Filter.apply(ResourceFilter<?> filter, List<Resource<?>> resources)
    */

   /**
    * Return the common name of the resource. If it's a file, for instance, just the file name.
    * 
    * @return The name of the resource.
    */
   public String getName();

   /**
    * Return the fully qualified name of the resource (if applicable). In the case of a file, this would normally be the
    * full path name.
    * 
    * @return The fully qualified name.
    */
   public String getFullyQualifiedName();

   /**
    * Get the parent of the current resource. Returns null if the current resource is the project root.
    * 
    * @return An instance of the resource parent.
    */
   public Resource<?> getParent();

   /**
    * Create a new resource instance for the target resource reference of the type that this current resource is.
    * 
    * @param file The target reference to create the resource instance from.
    * @return A new resource.
    */
   public Resource<T> createFrom(T file);

   /**
    * Return a list of child resources of the current resource.
    */
   public List<Resource<?>> listResources();

   /**
    * Return a list of child resources of the current resource matching the given {@link ResourceFilter}.
    */
   public List<Resource<?>> listResources(ResourceFilter filter);

   public T getUnderlyingResourceObject();

   public InputStream getResourceInputStream();

   /**
    * Get a child of this resource. Returns null if no child by the given name can be found.
    */
   public Resource<?> getChild(String name);

   public void setFlag(ResourceFlag flag);

   public void unsetFlag(ResourceFlag flag);

   public boolean isFlagSet(ResourceFlag flag);

   public boolean exists();

   public Set<ResourceFlag> getFlags();

   /**
    * Ask this {@link Resource} if it is actually a resource of the given type; if it is, return a new reference to the
    * resource as the given type, otherwise return null.
    */
   <R> R reify(Class<? extends Resource<?>> type);
}
