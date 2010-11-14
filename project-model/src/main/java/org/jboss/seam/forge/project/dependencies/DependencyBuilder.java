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
package org.jboss.seam.forge.project.dependencies;

import java.util.List;

import org.jboss.seam.forge.project.PackagingType;

/**
 * Builder to create {@link Dependency} objects. This class implements {@link Dependency} for easy consumption. (I.e.:
 * Use this class wherever you need to create and use a new {@link Dependency})
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class DependencyBuilder implements Dependency
{
   private final DependencyImpl dep = new DependencyImpl();

   protected DependencyBuilder()
   {
   }

   /**
    * Obtain a new {@link DependencyBuilder} instance.
    */
   public static DependencyBuilder create()
   {
      return new DependencyBuilder();
   }

   /**
    * @param identifier of the form "groupId:artifactId:version:scope
    */
   public static DependencyBuilder create(final String identifier)
   {
      DependencyBuilder dependencyBuilder = new DependencyBuilder();

      if (identifier != null)
      {
         String[] split = identifier.split(":");
         if (split.length > 0)
         {
            dependencyBuilder.setGroupId(split[0].trim());
         }
         if (split.length > 1)
         {
            dependencyBuilder.setArtifactId(split[1].trim());
         }
         if (split.length > 2)
         {
            dependencyBuilder.setVersion(split[2].trim());
         }
         if (split.length > 3)
         {
            ScopeType scopeType = ScopeType.getScopeType(split[3].trim());
            dependencyBuilder.setScopeType(scopeType == ScopeType.OTHER ? null : scopeType);
         }
         if (split.length > 4)
         {
            PackagingType packaging = PackagingType.from(split[4].trim());
            dependencyBuilder.setPackagingType(packaging == PackagingType.OTHER ? null : packaging);
         }
      }

      return dependencyBuilder;
   }

   public DependencyBuilder setGroupId(final String groupId)
   {
      dep.setGroupId(groupId);
      return this;
   }

   public DependencyBuilder setArtifactId(final String artifactId)
   {
      dep.setArtifactId(artifactId);
      return this;
   }

   public DependencyBuilder setVersion(final String version)
   {
      dep.setVersion(version);
      return this;
   }

   public DependencyBuilder setScopeType(final ScopeType scope)
   {
      dep.setScopeType(scope);
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

   @Override
   public String getVersion()
   {
      return dep.getVersion();
   }

   @Override
   public ScopeType getScopeType()
   {
      return dep.getScopeType();
   }

   @Override
   public List<Dependency> getExcludedDependencies()
   {
      return dep.getExcludedDependencies();
   }

   public ExcludedDependencyBuilder addExclusion()
   {
      return addExclusion(this);
   }

   ExcludedDependencyBuilder addExclusion(final DependencyBuilder parent)
   {
      ExcludedDependencyBuilder exclusion = ExcludedDependencyBuilder.create(parent);
      dep.getExcludedDependencies().add(exclusion);
      return exclusion;
   }

   @Override
   public PackagingType getPackagingType()
   {
      return dep.getPackagingType();
   }

   public DependencyBuilder setPackagingType(final PackagingType type)
   {
      dep.setPackagingType(type);
      return this;
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
      DependencyBuilder other = (DependencyBuilder) obj;
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
