/*
 * JBoss, by Red Hat.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.seam.forge.shell.util;

/**
 * @author Mike Brock .
 */
public class PluginRef
{
   private String name;
   private String author;
   private String description;
   private String artifact;
   private String homeRepo;

   public PluginRef(String name, String author, String description, String artifact, String homeRepo)
   {
      this.name = name;
      this.author = author;
      this.description = description;
      this.artifact = artifact;
      this.homeRepo = homeRepo;
   }

   public String getName()
   {
      return name;
   }

   public String getAuthor()
   {
      return author;
   }

   public String getDescription()
   {
      return description;
   }

   public String getArtifact()
   {
      return artifact;
   }

   public String getHomeRepo()
   {
      return homeRepo;
   }
}
