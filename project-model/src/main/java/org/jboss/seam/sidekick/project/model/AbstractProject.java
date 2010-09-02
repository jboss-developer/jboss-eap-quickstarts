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
package org.jboss.seam.sidekick.project.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.ProjectModelException;
import org.jboss.seam.sidekick.project.events.JavaFileCreated;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class AbstractProject implements Project
{
   @Inject
   Event<JavaFileCreated> event;

   private File createJavaFile(final File sourceFolder, final String classPackage, final String className,
            final char[] data)
   {
      BufferedWriter writer = null;
      try
      {
         String pkg = getDefaultSourceFolder() + File.separator + classPackage.replaceAll("\\.", File.separator);

         File file = new File(pkg + File.separator + className + ".java");

         if (!file.mkdirs())
         {
            throw new IOException("Failed to create required directory structure for file: " + file);
         }

         file.delete();

         if (!file.createNewFile())
         {
            throw new IOException("Failed to create file because it already exists: " + file);
         }

         writer = new BufferedWriter(new FileWriter(file));
         writer.write(data);
         writer.close();

         // event.fire(new JavaFileCreated(file));
         return file;
      }
      catch (IOException e)
      {
         throw new ProjectModelException(e);
      }
   }

   private File createJavaFile(final String classPackage, final String className, final String data)
   {
      return createJavaFile(getDefaultSourceFolder(), classPackage, className, data.toCharArray());
   }

   @Override
   public File createJavaFile(final JavaClass clazz)
   {
      // event.fire(clazz);
      return createJavaFile(clazz.getPackage(), clazz.getName(), clazz.toString());
   }

   @Override
   public boolean delete(final File file)
   {
      if (file.isDirectory())
      {
         for (File c : file.listFiles())
         {
            if (!delete(c))
            {
               throw new ProjectModelException("Could not delete file or folder: " + file);
            }
         }
      }
      return file.delete();
   }

}
