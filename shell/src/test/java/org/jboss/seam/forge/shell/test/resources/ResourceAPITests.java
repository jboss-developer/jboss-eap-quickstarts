package org.jboss.seam.forge.shell.test.resources;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.shell.util.PathspecParser;
import org.jboss.seam.forge.shell.util.ResourceUtil;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@Singleton
@RunWith(Arquillian.class)
public class ResourceAPITests extends AbstractShellTest
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
   public void testPathNavigation2()
   {
      DirectoryResource expect = new DirectoryResource(factory, new File("").getAbsoluteFile());
      DirectoryResource r = new DirectoryResource(factory, new File("").getAbsoluteFile());

      Assert.assertEquals(expect, ResourceUtil.parsePathspec(factory, r, ".").iterator().next());
   }

   @Test
   public void testPathParser()
   {
      DirectoryResource expect = new DirectoryResource(factory, new File("/"));
      DirectoryResource root = new DirectoryResource(factory, new File("").getAbsoluteFile());

      List<Resource<?>> r = ResourceUtil.parsePathspec(factory, root, "/");
      Resource<?> actual = r.iterator().next();

      Assert.assertEquals(expect.getFullyQualifiedName(), actual.getFullyQualifiedName());
   }

   @Test
   public void testWildCards()
   {

      DirectoryResource r = new DirectoryResource(factory, new File("").getAbsoluteFile());

      for (Resource<?> res : ResourceUtil.parsePathspec(factory, r, "*"))
      {
         System.out.println(res);
      }
   }

   @Test
   public void testWildCards2()
   {

      DirectoryResource r = new DirectoryResource(factory, new File("").getAbsoluteFile());

      for (Resource<?> res : ResourceUtil.parsePathspec(factory, r, "*.java"))
      {
         System.out.println(res);
      }
   }

   @Test
   public void testDeepSearch()
   {
      DirectoryResource root = new DirectoryResource(factory, new File("").getAbsoluteFile());
      List<Resource<?>> results = new PathspecParser(factory, root, "BaseEvent.java").search();

      for (Resource<?> r : results)
      {
         System.out.println(r);
      }
   }

}
