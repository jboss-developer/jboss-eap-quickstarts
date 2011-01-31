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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jboss.seam.forge.project.PackagingType;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class DependencyBuilderTest
{
   @Test
   public void testCreateWithIdentifier() throws Exception
   {
      DependencyBuilder dep = DependencyBuilder.create("org.jboss.seam.forge: seam-forge :9:test");

      assertEquals("org.jboss.seam.forge", dep.getGroupId());
      assertEquals("seam-forge", dep.getArtifactId());
      assertEquals("9", dep.getVersion());
      assertEquals(ScopeType.TEST, dep.getScopeTypeEnum());
   }

   @Test
   public void testCreateWithPartialIdentifier() throws Exception
   {
      DependencyBuilder dep = DependencyBuilder.create("org.jboss.seam.forge: seam-forge :9 ::pom");

      assertEquals("org.jboss.seam.forge", dep.getGroupId());
      assertEquals("seam-forge", dep.getArtifactId());
      assertEquals("9", dep.getVersion());
      assertNull(dep.getScopeType());
      assertEquals(PackagingType.BASIC, dep.getPackagingTypeEnum());
   }

   @Test
   public void testCreateWithPartialIdentifier2() throws Exception
   {
      DependencyBuilder dep = DependencyBuilder.create("org.jboss.seam.forge");

      assertEquals("org.jboss.seam.forge", dep.getGroupId());
      assertEquals(null, dep.getArtifactId());
      assertEquals(null, dep.getVersion());
      assertEquals(null, dep.getScopeType());
   }

   @Test
   public void testCreateWithPartialAndEmptyIdentifier() throws Exception
   {
      DependencyBuilder dep = DependencyBuilder.create("org.jboss.seam.forge::");

      assertEquals("org.jboss.seam.forge", dep.getGroupId());
      assertEquals(null, dep.getArtifactId());
      assertEquals(null, dep.getVersion());
      assertEquals(null, dep.getScopeType());
   }
}
