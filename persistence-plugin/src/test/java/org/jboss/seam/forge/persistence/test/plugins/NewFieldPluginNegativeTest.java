package org.jboss.seam.forge.persistence.test.plugins;

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

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.persistence.PersistenceFacet;
import org.jboss.seam.forge.persistence.test.plugins.util.AbstractJPATest;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.util.Packages;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;

import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class NewFieldPluginNegativeTest extends AbstractJPATest
{

   @Test(expected = FileNotFoundException.class)
   public void testNewFieldWithoutEntityDoesNotCreateFile() throws Exception
   {
      Project project = getProject();
      String entityName = "Goofy";

      queueInputLines(entityName);
      getShell().execute("new-field int --fieldName gamesPlayed");

      String pkg = project.getFacet(PersistenceFacet.class).getEntityPackage() + "." + entityName;
      String path = Packages.toFileSyntax(pkg) + ".java";

      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      java.getJavaClass(path); // exception here or die
      fail();
   }
}
