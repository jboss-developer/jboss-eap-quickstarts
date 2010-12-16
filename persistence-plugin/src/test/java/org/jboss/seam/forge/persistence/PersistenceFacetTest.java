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

package org.jboss.seam.forge.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.persistence.test.plugins.util.AbstractJPATest;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceModel;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceUnit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class PersistenceFacetTest extends AbstractJPATest
{
   @Test
   public void testCDintoProjectRegistersScaffoldingFacet() throws Exception
   {
      Shell shell = getShell();
      Project project = getProject();

      PersistenceFacet persistence = project.getFacet(PersistenceFacet.class);
      assertNotNull(persistence);

      shell.execute("cd /");
      assertNull(getProject());

      shell.execute("cd - ");
      assertNotNull(getProject());

      project = getProject();
      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
      assertNotNull(javaSourceFacet);

      persistence = project.getFacet(PersistenceFacet.class);
      assertNotNull(persistence);
   }

   @Test
   public void testCanWritePersistenceConfigFile() throws Exception
   {
      Project project = getProject();

      PersistenceFacet persistence = project.getFacet(PersistenceFacet.class);
      assertNotNull(persistence);

      PersistenceModel model = persistence.getConfig();
      PersistenceUnit unit = model.getPersistenceUnits().get(0);

      assertEquals("default", unit.getName());
      unit.setName("not-default");

      persistence.saveConfig(model);

      unit = model.getPersistenceUnits().get(0);
      assertEquals("not-default", unit.getName());

      assertEquals("2.0", persistence.getConfig().getVersion());
   }
}
