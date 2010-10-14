package org.jboss.seam.forge.project.resources.builtin;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.ResourceIOException;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@ResourceHandles("*.java")
public class JavaResource extends FileResource
{
   private volatile JavaClass javaClass;

   public JavaResource()
   {
   }

   public JavaResource(File file)
   {
      super(file);
   }

   @Override
   public synchronized List<Resource<?>> listResources(ResourceFactory factory)
   {
      if (javaClass == null)
      {
         try
         {
            javaClass = JavaParser.parse(file);
         }
         catch (FileNotFoundException e)
         {
            return Collections.emptyList();
         }
      }

      List<Resource<?>> list = new LinkedList<Resource<?>>();

      for (Field field : javaClass.getFields()) {
         list.add(new JavaFieldResource(this, field)); 
      }

      for (Method method : javaClass.getMethods()) {
         list.add(new JavaMethodResource(this, method));
      }

      return list;
   }

   @Override
   public JavaResource createFrom(File file)
   {
      return new JavaResource(file);
   }
}


