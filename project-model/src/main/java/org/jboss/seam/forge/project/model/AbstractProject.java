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
package org.jboss.seam.forge.project.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.ProjectModelException;
import org.jboss.seam.forge.project.facets.FacetNotFoundException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class AbstractProject implements Project
{
   private final List<Facet> facets = new ArrayList<Facet>();

   @Override
   public boolean delete(final File file)
   {
      // TODO event.fire(File deleted)
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

   @Override
   public void writeFile(String string, File file)
   {
      if (string == null)
      {
         string = "";
      }
      writeFile(string.toCharArray(), file);
   }

   @Override
   public void writeFile(final char[] data, final File file)
   {
      BufferedWriter writer = null;
      File tempFile = null;
      try
      {
         if (!file.exists())
         {
            mkdirs(file);

            file.delete();
            if (!file.createNewFile())
            {
               throw new IOException("Failed to create file: " + file);
            }
         }
         else
         {
            tempFile = File.createTempFile(AbstractProject.class.getName(), null);
            File origin = file.getAbsoluteFile();
            if (!origin.renameTo(tempFile))
            {
               throw new IOException("Failed to update file because a temporary file could not be created: " + file);
            }
         }

         file.delete();

         writer = new BufferedWriter(new FileWriter(file));
         writer.write(data);
         writer.close();

         // FIXME need a way for these classes to access a writer
         System.out.println("Wrote " + file.getCanonicalPath());

      }
      catch (IOException e)
      {
         if ((tempFile != null) && !file.exists())
         {
            tempFile.renameTo(file);
         }

         throw new ProjectModelException(e);
      }
      finally
      {
         if (tempFile != null)
         {
            tempFile.delete();
         }
      }
   }

   @Override
   public void mkdirs(File file)
   {
      if (!file.exists())
      {
         if (!file.mkdirs())
         {
            throw new ProjectModelException(
                  new IOException("Failed to create required directory structure for file: " + file));
         }
      }
   }

   @Override
   public boolean hasFacet(final Class<? extends Facet> type)
   {
      try
      {
         getFacet(type);
         return true;
      }
      catch (FacetNotFoundException e)
      {
         return false;
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public <F extends Facet> F getFacet(final Class<F> type)
   {
      Facet result = null;
      for (Facet facet : facets)
      {
         if ((facet != null) && type.isAssignableFrom(facet.getClass()))
         {
            result = facet;
            break;
         }
      }
      if (result == null)
      {
         throw new FacetNotFoundException("The requested facet of type [" + type.getName()
                  + "] was not found. The facet is not installed.");
      }
      return (F) result;
   }

   @Override
   public List<Facet> getFacets()
   {
      return facets;
   }

   @Override
   @SuppressWarnings("unchecked")
   public <F extends Facet> List<F> getFacets(final Class<F> type)
   {
      List<F> result = new ArrayList<F>();

      for (Facet facet : facets)
      {
         if ((facet != null) && facet.getClass().isAssignableFrom(type))
         {
            result.add((F) facet);
         }
      }

      return result;
   }

   @Override
   public Project registerFacet(final Facet facet)
   {
      if (facet == null)
      {
         throw new IllegalArgumentException("Attempted to register 'null' as a Facet; Facets cannot be null.");
      }

      if (facet.getDependencies() != null)
      {
         for (Class<? extends Facet> type : facet.getDependencies())
         {
            if (!hasFacet(type))
            {
               throw new IllegalStateException("Attempting to register a Facet that has missing dependencies: [" + facet.getClass().getSimpleName() + " requires -> " + type.getSimpleName() + "]");
            }
         }
      }

      facet.init(this);
      if (facet.isInstalled() && !hasFacet(facet.getClass()))
      {
         facets.add(facet);
      }
      return this;
   }

   @Override
   public Project installFacet(final Facet facet)
   {
      facet.init(this);
      if (!facet.isInstalled() && !hasFacet(facet.getClass()))
      {
         facet.install();
         facets.add(facet);
      }
      else if (!hasFacet(facet.getClass()))
      {
         registerFacet(facet);
      }
      return this;
   }
}
