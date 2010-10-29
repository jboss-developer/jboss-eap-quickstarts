package org.jboss.seam.forge.project.resources.builtin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@ResourceHandles("*.java")
public class JavaResource extends FileResource
{
   private volatile JavaClass javaClass;

   @Inject
   public JavaResource(final ResourceFactory factory)
   {
      super(factory);
   }

   public JavaResource(final ResourceFactory factory, final File file)
   {
      super(factory, file);
   }

   @Override
   public synchronized List<Resource<?>> listResources()
   {
      try
      {
         lazyInitialize();
      }
      catch (FileNotFoundException e)
      {
         return Collections.emptyList();
      }

      List<Resource<?>> list = new LinkedList<Resource<?>>();

      for (Field field : javaClass.getFields())
      {
         list.add(new JavaFieldResource(this, field));
      }

      for (Method method : javaClass.getMethods())
      {
         list.add(new JavaMethodResource(this, method));
      }

      return list;
   }

   private void lazyInitialize() throws FileNotFoundException
   {
      if (javaClass == null)
      {
         javaClass = JavaParser.parse(file);
      }
   }

   public JavaClass getJavaClass() throws FileNotFoundException
   {
      lazyInitialize();
      return javaClass;
   }

   @Override
   public JavaResource createFrom(final File file)
   {
      return new JavaResource(resourceFactory, file);
   }

   @Override
   public String toString()
   {
      try
      {
         lazyInitialize();
      }
      catch (FileNotFoundException e)
      {
         return "[File not found: " + file + "]";
      }
      return javaClass.getName() + ".java";
   }
}
