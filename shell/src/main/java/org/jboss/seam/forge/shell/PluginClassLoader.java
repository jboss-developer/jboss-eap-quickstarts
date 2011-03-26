/*
 * JBoss, by Red Hat.
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
package org.jboss.seam.forge.shell;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.jboss.seam.forge.shell.PluginJar.IllegalNameException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class PluginClassLoader extends URLClassLoader
{
   private final File file;
   private final PluginJar jar;

   public PluginClassLoader(File file) throws MalformedURLException
   {
      this(file, null);
   }

   public PluginClassLoader(File file, ClassLoader parent) throws IllegalNameException, MalformedURLException
   {
      super(new URL[] { file.toURI().toURL() }, parent);

      this.file = file;

      this.jar = new PluginJar(file.getName());
   }

   public File getFile()
   {
      return file;
   }

   public String getPluginName()
   {
      return jar.getName();
   }

   public int getPluginVersion()
   {
      return jar.getVersion();
   }

   @Override
   public String toString()
   {
      return jar.getFullName();
   }
}
