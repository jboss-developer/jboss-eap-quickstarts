/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.forge.shell.test.plugins.builtin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;

/**
 * LsMavenPomPluginTestCase
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class NewProjectPluginTest extends AbstractShellTest
{
   @Test
   public void testCreateProject() throws Exception
   {
      Shell shell = getShell();
      DirectoryResource origin = shell.getCurrentDirectory();

      initializeJavaProject();
      Project project = getProject();

      DirectoryResource created = shell.getCurrentDirectory();

      assertEquals(created, project.getProjectRoot());
      assertNotSame(origin, created);
   }
}
