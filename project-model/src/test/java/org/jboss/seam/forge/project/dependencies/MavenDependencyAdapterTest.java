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

import org.apache.maven.model.Exclusion;
import org.jboss.seam.forge.project.PackagingType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MavenDependencyAdapterTest
{
   org.apache.maven.model.Dependency mvn;
   Dependency dep;

   public MavenDependencyAdapterTest()
   {
      dep = DependencyBuilder.create()
            .setArtifactId("seam-forge")
            .setGroupId("org.jboss.seam.forge")
            .addExclusion()
            .setArtifactId("sub-module")
            .setGroupId("org.jboss.seam.forge")
            .addExclusion()
            .setArtifactId("sub-module-2")
            .setGroupId("org.jboss.seam.forge")
            .setScope(ScopeType.COMPILE)
            .setVersion("9")
            .setPackagingType(PackagingType.WAR);

      mvn = new org.apache.maven.model.Dependency();
      mvn.setArtifactId("seam-forge");
      mvn.setGroupId("org.jboss.seam.forge");
      mvn.setVersion("9");
      mvn.setScope("ComPiLe");

      Exclusion ex1 = new Exclusion();
      ex1.setArtifactId("sub-module");
      ex1.setGroupId("org.jboss.seam.forge");
      mvn.addExclusion(ex1);

      Exclusion ex2 = new Exclusion();
      ex2.setArtifactId("sub-module-2");
      ex2.setGroupId("org.jboss.seam.forge");
      mvn.addExclusion(ex2);
   }

   @Test
   public void testConvertFromMVNToForge() throws Exception
   {
      MavenDependencyAdapter toForge = new MavenDependencyAdapter(mvn);
      MavenDependencyAdapter toMvn = new MavenDependencyAdapter(dep);

      assertEquals(toForge.getArtifactId(), toMvn.getArtifactId());
      assertEquals(toForge.getGroupId(), toMvn.getGroupId());
      assertEquals(toForge.getVersion(), toMvn.getVersion());
      assertEquals(toForge.getScopeType(), toMvn.getScopeType());
      assertEquals(toForge.getScope(), toMvn.getScope());
   }

   @Test
   public void testExclusionsConvertProperly() throws Exception
   {
      MavenDependencyAdapter toForge = new MavenDependencyAdapter(mvn);
      MavenDependencyAdapter toMvn = new MavenDependencyAdapter(dep);

      assertEquals(toForge.getExcludedDependencies(), toMvn.getExcludedDependencies());
      assertEquals(toForge.getExclusions().get(0).getArtifactId(), toMvn.getExclusions().get(0).getArtifactId());
      assertEquals(toForge.getExclusions().get(0).getGroupId(), toMvn.getExclusions().get(0).getGroupId());
      assertEquals(toForge.getExclusions().get(1).getArtifactId(), toMvn.getExclusions().get(1).getArtifactId());
      assertEquals(toForge.getExclusions().get(1).getGroupId(), toMvn.getExclusions().get(1).getGroupId());
   }
}
