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

package org.jboss.seam.forge.test.project.services;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Singleton;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
@RunWith(Arquillian.class)
public class ProjectFactoryTest extends AbstractShellTest
{
   @Test
   public void testCDintoProjectRegistersFacets() throws Exception
   {
      Shell shell = getShell();
      initializeJavaProject();

      assertNotNull(getProject());

      shell.execute("cd /");

      assertNull(getProject());

      shell.execute("cd -");

      Project project = getProject();
      assertNotNull(project);
      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);

      assertNotNull(javaSourceFacet);
   }
}
