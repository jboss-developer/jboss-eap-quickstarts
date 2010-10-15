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
package org.jboss.seam.forge.shell.plugins.builtin;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
@Named("ls")
@Help("Prints the contents current directory.")
public class LsPlugin implements Plugin
{
   private final Shell shell;
   private final ResourceFactory resourceFactory;

   private final DateFormat format = new SimpleDateFormat("MMM WW HH:mm");

   @Inject
   public LsPlugin(ResourceFactory resourceFactory, Shell shell)
   {
      this.resourceFactory = resourceFactory;
      this.shell = shell;
   }

   @DefaultCommand
   public void run(@Option(flagOnly = true, name = "all", shortName = "a", required = false) boolean showAll)
   {
      Resource<?> resource = shell.getCurrentResource();

      int width = shell.getWidth();

      List<Resource<?>> childResources = resource.listResources();
      List<String> listData = new LinkedList<String>();

      int maxLength = 0;

      String el;
      for (Resource r : childResources)
      {
         el = r.toString();

         if (showAll || !el.startsWith("."))
         {
            listData.add(el);
            if (el.length() > maxLength) maxLength = el.length();
         }
      }

      int cols = width / (maxLength + 4);
      int colSize = width / cols;

      if (cols == 0)
      {
         colSize = width;
         cols = 1;
      }

      int i = 0;
      for (String s : listData)
      {
         shell.print(s);
         shell.print(pad(colSize - s.length()));
         if (++i == cols)
         {
            shell.println();
            i = 0;
         }
      }
      shell.println();
   }

   private String pad(int amount)
   {
      char[] padding = new char[amount];
      for (int i = 0; i < amount; i++)
      {
         padding[i] = ' ';
      }
      return new String(padding);
   }

}
