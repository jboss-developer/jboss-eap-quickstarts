package org.jboss.seam.forge.test.project.resources;


import junit.framework.Assert;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.test.project.util.ProjectModelTest;
import org.junit.Test;

import java.io.File;

public class ResourceAPITests extends ProjectModelTest
{
   @Test
   public void testPathNavigation()
   {
      DirectoryResource expect = new DirectoryResource(new File("").getAbsoluteFile().getParentFile().getParentFile());
      DirectoryResource r = new DirectoryResource(new File("").getAbsoluteFile());

      Assert.assertEquals(expect, ResourceUtil.parsePathspec(r, "../.."));
   }

   @Test
   public void testPathParser() {
      DirectoryResource expect = new DirectoryResource(new File("/"));
      DirectoryResource root = new DirectoryResource(new File("").getAbsoluteFile());

      Resource r = ResourceUtil.parsePathspec(root, "/");

      Assert.assertEquals(expect, r);
   }
}
