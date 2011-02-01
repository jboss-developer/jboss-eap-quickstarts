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

package org.jboss.seam.forge.project.dependencies;

import java.util.List;

import org.jboss.seam.forge.project.PackagingType;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ExcludedDependencyBuilder implements Dependency
{
   private final DependencyBuilder parent;
   private final DependencyImpl dep = new DependencyImpl();

   protected ExcludedDependencyBuilder(final DependencyBuilder parent)
   {
      this.parent = parent;
   }

   public static ExcludedDependencyBuilder create(final DependencyBuilder parent)
   {
      return new ExcludedDependencyBuilder(parent);
   }

   public ExcludedDependencyBuilder setGroupId(final String groupId)
   {
      dep.setGroupId(groupId);
      return this;
   }

   public ExcludedDependencyBuilder setArtifactId(final String artifactId)
   {
      dep.setArtifactId(artifactId);
      return this;
   }

   @Override
   public String getArtifactId()
   {
      return dep.getArtifactId();
   }

   @Override
   public String getGroupId()
   {
      return dep.getGroupId();
   }

   /**
    * Not implemented for Exclusions. Always returns <code>""</code>.
    */
   @Override
   public String getVersion()
   {
      return "";
   }

   /**
    * Not implemented for Exclusions. Always returns <code>null</code>.
    */
   @Override
   public String getScopeType()
   {
      throw new IllegalStateException("Not implemented for Exclusions");
   }

   @Override
   public PackagingType getPackagingTypeEnum()
   {
      throw new IllegalStateException("Not implemented for Exclusions");
   }

   @Override
   public ScopeType getScopeTypeEnum()
   {
      throw new IllegalStateException("Not implemented for Exclusions");
   }

   @Override
   public List<Dependency> getExcludedDependencies()
   {
      return dep.getExcludedDependencies();
   }

   /*
    * DependencyBuilder scoped methods.
    */

   public ExcludedDependencyBuilder addExclusion()
   {
      return parent.addExclusion(parent);
   }

   public DependencyBuilder setVersion(final String version)
   {
      parent.setVersion(version);
      return parent;
   }

   public DependencyBuilder setScope(final ScopeType scope)
   {
      parent.setScopeType(scope);
      return parent;
   }

   @Override
   public String getPackagingType()
   {
      throw new IllegalStateException("Not implemented for Exclusions");
   }

   @Override
   public String toString()
   {
      return DependencyBuilder.toString(this);
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((dep == null) ? 0 : dep.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      ExcludedDependencyBuilder other = (ExcludedDependencyBuilder) obj;
      if (dep == null)
      {
         if (other.dep != null)
         {
            return false;
         }
      }
      else if (!dep.equals(other.dep))
      {
         return false;
      }
      return true;
   }
}
