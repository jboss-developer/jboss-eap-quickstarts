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

package org.jboss.seam.forge.shell.plugins.builtin;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.shell.util.FormatCallback;
import org.jboss.seam.forge.shell.util.GeneralUtils;
import org.jboss.seam.forge.shell.util.ShellColor;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jboss.seam.forge.shell.util.GeneralUtils.printOutColumns;
import static org.jboss.seam.forge.shell.util.GeneralUtils.printOutTables;

/**
 * Lists directory contents for filesystem based directories. This is a
 * simplified version of the UNIX 'ls' command and currently supports the -totalLines and
 * -a flags, as in unix.
 * 
 * @author Mike Brock
 */
@Named("ls")
@Topic("File & Resources")
@ResourceScope(DirectoryResource.class)
@Help("Prints the contents current directory.")
public class LsPlugin implements Plugin
{
   private final Shell shell;

   private static final long yearMarker;
   private static final SimpleDateFormat dateFormatOld = new SimpleDateFormat("MMM d yyyy");
   private static final SimpleDateFormat dateFormatRecent = new SimpleDateFormat("MMM d HH:mm");

   static
   {
      Calendar c = Calendar.getInstance();
      c.setTimeInMillis(System.currentTimeMillis());
      c.set(Calendar.MONTH, 0);
      c.set(Calendar.DAY_OF_MONTH, 0);
      c.set(Calendar.HOUR, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);

      yearMarker = c.getTimeInMillis();
   }

   @Inject
   public LsPlugin(final Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void run(@Option(flagOnly = true, name = "all", shortName = "a", required = false) final boolean showAll,
                   @Option(flagOnly = true, name = "list", shortName = "l", required = false) final boolean list,
                   @Option(description = "path", defaultValue = ".") Resource<?>[] paths,
                   final PipeOut out)
   {

      Map<String, List<String>> sortMap = new TreeMap<String, List<String>>();
      List<String> listBuild = new LinkedList<String>();

      for (Resource<?> resource : paths)
      {
         List<Resource<?>> childResources;

         /**
          * Check to see if the way this resource was resolved was by a
          * wildcard, in which case we don't expand into it's children.
          * Otherwise, if it's fully qualified we recurse into that directory
          * and list all those files.
          */
         if (!resource.isFlagSet(ResourceFlag.AmbiguouslyQualified) && resource.isFlagSet(ResourceFlag.Node))
         {
            childResources = resource.listResources();
         }
         else
         {
            if (resource.exists())
            {
               childResources = Collections.<Resource<?>> singletonList(resource);
            }
            else
            {
               return;
            }
         }

         String el;
         File file;

         if (list)
         {
            /**
             * List-view implementation.
             */
            int fileCount = 0;
            boolean dir;
            List<String> subList;
            for (Resource<?> r : childResources)
            {
               sortMap.put(el = r.getName(), subList = new ArrayList<String>());
               file = (File) r.getUnderlyingResourceObject();

               if (dir = (r instanceof DirectoryResource))
               {
                  el += "/";
               }

               if (showAll || !el.startsWith("."))
               {
                  StringBuilder permissions = new StringBuilder(dir ? "d" : "-")
                        .append(file.canRead() ? 'r' : '-')
                        .append(file.canWrite() ? 'w' : '-')
                        .append(file.canExecute() ? 'x' : '-')
                        .append("------");

                  subList.add(permissions.toString());
                  subList.add("owner"); // not supported
                  subList.add(" users "); // not supported
                  subList.add(String.valueOf(file.length()));
                  subList.addAll(Arrays.asList(getDateString(file.lastModified())));
                  subList.add(el);

                  if (!dir)
                  {
                     fileCount++;
                  }
               }
            }

            for (List<String> sublist : sortMap.values())
            {
               listBuild.addAll(sublist);
            }

            out.println("total " + fileCount);
         }
         else
         {
            for (Resource<?> r : childResources)
            {
               el = r.getName();

               if (r instanceof DirectoryResource)
               {
                  el += "/";
               }

               if (showAll || !el.startsWith("."))
               {
                  listBuild.add(el);
               }
            }
         }
      }

      /**
       * print the results.
       */

      if (list)
      {
         FormatCallback formatCallback = new FormatCallback()
         {
            @Override
            public String format(int column, String value)
            {
               if ((column == 7) && value.endsWith("/"))
               {
                  return shell.renderColor(ShellColor.BLUE, value);
               }
               else
               {
                  return value;
               }
            }
         };

         printOutTables(
               listBuild,
               new boolean[] { false, false, false, true, false, false, true, false },
               out,
               formatCallback);
      }
      else
      {
         FormatCallback formatCallback = new FormatCallback()
         {
            @Override
            public String format(int column, String value)
            {
               if (value.endsWith("/"))
               {
                  return out.renderColor(ShellColor.BLUE, value);
               }
               else
               {
                  return value;
               }
            }
         };

         if (out.isPiped())
         {
            GeneralUtils.OutputAttributes attr = new GeneralUtils.OutputAttributes(120, 1);
            printOutColumns(listBuild, ShellColor.NONE, out, attr, null, false);
         }
         else
         {
            printOutColumns(listBuild, out, shell, formatCallback, false);
         }
      }
   }

   private static String[] getDateString(long time)
   {
      if (time < yearMarker)
      {
         return dateFormatOld.format(new Date(time)).split(" ");
      }
      else
      {
         return dateFormatRecent.format(new Date(time)).split(" ");
      }
   }
}
