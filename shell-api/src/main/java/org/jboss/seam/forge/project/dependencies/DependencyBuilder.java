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

import org.jboss.seam.forge.project.packaging.PackagingType;

/**
 * Builder to create {@link Dependency} objects. This class implements {@link Dependency} for easy consumption. (I.e.:
 * Use this class wherever you need to create and use a new {@link Dependency})
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
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

   public static DependencyBuilder create(final Dependency dep)
   {
      DependencyBuilder builder = new DependencyBuilder();
      builder.setGroupId(dep.getGroupId());
      builder.setArtifactId(dep.getArtifactId());
      builder.setVersion(dep.getVersion());
      builder.setPackagingType(dep.getPackagingType());
      builder.setScopeType(dep.getScopeType());
      return builder;
   }

   /**
    * Return true if the groupId and artifactId of the two given dependencies are equal.
    */
   public static boolean areEquivalent(Dependency l, Dependency r)
   {
      if (l == r)
      {
         return true;
      }
      if (l == null && r == null)
      {
         return true;
      }
      else if (l == null || r == null)
      {
         return false;
      }

      return !(l.getArtifactId() != null ? !l.getArtifactId().equals(r.getArtifactId()) : r.getArtifactId() != null) &&
               !(l.getGroupId() != null ? !l.getGroupId().equals(r.getGroupId()) : r.getGroupId() != null);

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
            String trimmed = split[3].trim();
            ScopeType scopeType = ScopeType.from(trimmed);
            if (ScopeType.OTHER.equals(scopeType))
            {
               dependencyBuilder.setScopeType(trimmed == null ? null : trimmed);
            }
            else
            {
               dependencyBuilder.setScopeType(scopeType);
            }
         }
         if (split.length > 4)
         {
            String trimmed = split[4].trim();
            PackagingType packaging = PackagingType.from(trimmed);
            if (PackagingType.OTHER.equals(packaging))
            {
               dependencyBuilder.setPackagingType(trimmed == null ? null : trimmed);
            }
            else
            {
               dependencyBuilder.setPackagingType(packaging);
            }
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

   public DependencyBuilder setScopeType(final String scope)
   {
      dep.setScopeType(scope);
      return this;
   }

   public DependencyBuilder setPackagingType(final PackagingType type)
   {
      dep.setPackagingType(type);
      return this;
   }

   public DependencyBuilder setPackagingType(final String type)
   {
      dep.setPackagingType(type);
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
   public String getScopeType()
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
   public String getPackagingType()
   {
      return dep.getPackagingType();
   }

   @Override
   public PackagingType getPackagingTypeEnum()
   {
      return dep.getPackagingTypeEnum();
   }

   @Override
   public ScopeType getScopeTypeEnum()
   {
      return dep.getScopeTypeEnum();
   }

   /**
    * Convenience method which should be used to convert a {@link Dependency} object into its id representation, for
    * example: "groupId:artifactId:version"
    * 
    * @see {@link Dependency#toCoordinates()}
    */
   public static String toId(Dependency dep)
   {
      String gav = dep.getGroupId() + ":" + dep.getArtifactId();
      if (dep.getVersion() != null)
      {
         gav += ":" + dep.getVersion();
      }
      return gav;
   }

   /**
    * Convenience method which should be used to convert a {@link Dependency} object into its string representation, for
    * example: "groupId:artifactId:version:scope:packaging"
    */
   public static String toString(final Dependency dep)
   {
      String gav = dep.toCoordinates();
      if (dep.getScopeType() != null)
      {
         gav += ":" + dep.getScopeType();
      }
      if (dep.getPackagingType() != null)
      {
         gav += ":" + dep.getPackagingType();
      }
      return gav;
   }

   @Override
   public String toCoordinates()
   {
      return toId(this);
   }

   @Override
   public String toString()
   {
      return DependencyBuilder.toString(dep);
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
