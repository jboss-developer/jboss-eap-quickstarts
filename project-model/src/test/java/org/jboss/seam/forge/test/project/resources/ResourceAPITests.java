package org.jboss.seam.forge.test.project.resources;


import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.test.project.util.ProjectModelTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

@Singleton
@RunWith(Arquillian.class)
public class ResourceAPITests extends ProjectModelTest
{
   private ResourceFactory factory;

   @Inject
   public ResourceAPITests(ResourceFactory factory)
   {
      this.factory = factory;
   }

   @Test
   public void testPathNavigation()
   {
      DirectoryResource expect = new DirectoryResource(new File("").getAbsoluteFile().getParentFile().getParentFile());
      DirectoryResource r = new DirectoryResource(new File("").getAbsoluteFile());

      Assert.assertEquals(expect, ResourceUtil.parsePathspec(factory, r, "../.."));
   }

   @Test
   public void testPathParser() {
      DirectoryResource expect = new DirectoryResource(new File("/"));
      DirectoryResource root = new DirectoryResource(new File("").getAbsoluteFile());

      Resource r = ResourceUtil.parsePathspec(factory, root, "/");

      Assert.assertEquals(expect, r);
   }
}
