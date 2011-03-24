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

package org.jboss.seam.forge.resources;

/**
 * @author Mike Brock .
 */
public enum ResourceFlag
{
   /**
    * The resource instance is a Prototype (a factory instance).  This instance is maintained by the framework
    * to spawn new instances, but does not actually represent a real resource.
    */
   Prototype,

   /**
    * The resource was qualified by an ambiguous qualifier (a wildcard) as opposed to being uniquely qualified.
    */
   AmbiguouslyQualified,

   /**
    * The resource represents a node, which contains or is at least capable of having children..
    */
   Node,

   /**
    * The resource is a leaf, and therefore has no children.
    */
   Leaf,

   /**
    * The resource is a physical file.
    */
   File,

   /**
    * The resource is a project source file.
    */
   ProjectSourceFile,

   /**
    * The resource is a test source file for the current project.
    */
   ProjectTestSourceFile
}
