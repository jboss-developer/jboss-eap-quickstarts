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
package org.jboss.seam.forge.project.facets.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.maven.model.Model;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.MavenFacet;
import org.jboss.seam.forge.project.facets.PackagingFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MavenPackagingFacet implements PackagingFacet
{
   private Project project;

   @Override
   public void setPackagingType(final PackagingType type)
   {
      MavenFacet mavenFacet = project.getFacet(MavenFacet.class);
      Model pom = mavenFacet.getPOM();
      pom.setPackaging(type.getType());
      mavenFacet.setPOM(pom);
   }

   @Override
   public PackagingType getPackagingType()
   {
      MavenFacet mavenFacet = project.getFacet(MavenFacet.class);
      Model pom = mavenFacet.getPOM();
      return new PackagingType(pom.getPackaging());
   }

   @Override
   public Project getProject()
   {
      return project;
   }

   @Override
   public Facet init(final Project project)
   {
      this.project = project;
      return this;
   }

   @Override
   public boolean isInstalled()
   {
      MavenFacet mavenFacet = project.getFacet(MavenFacet.class);
      return mavenFacet != null;
   }

   @Override
   public Facet install()
   {
      if (PackagingType.NONE.equals(getPackagingType()))
      {
         setPackagingType(PackagingType.BASIC);
      }
      project.registerFacet(this);
      return this;
   }

   @Override
   public Set<Class<? extends Facet>> getDependencies()
   {
      Set<Class<? extends Facet>> result = new HashSet<Class<? extends Facet>>();
      result.add(MavenFacet.class);
      return result;
   }
}
