package org.jboss.seam.forge.test.project.resources;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.test.project.util.ProjectModelTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@Singleton
@RunWith(Arquillian.class)
public class ResourceAPITests extends ProjectModelTest
{
   @Inject
   private ResourceFactory factory;

   @Test
   public void testPathNavigation()
   {
      DirectoryResource expect = new DirectoryResource(factory, new File("").getAbsoluteFile().getParentFile()
               .getParentFile());
      DirectoryResource r = new DirectoryResource(factory, new File("").getAbsoluteFile());

      Assert.assertEquals(expect, ResourceUtil.parsePathspec(factory, r, "../..").iterator().next());
   }

   @Test
   public void testPathParser()
   {
      DirectoryResource expect = new DirectoryResource(factory, new File("/"));
      DirectoryResource root = new DirectoryResource(factory, new File("").getAbsoluteFile());

      List<Resource<?>> r = ResourceUtil.parsePathspec(factory, root, "/");

      Assert.assertEquals(expect, r.iterator().next());
   }
}
