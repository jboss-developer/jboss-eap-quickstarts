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
import org.jboss.seam.forge.shell.plugins.*;
import org.mvel2.util.StringAppender;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * A simple port of the Unix grep command.
 *
 * @author Mike Brock .
 */
@Named("grep")
@Topic("File & Resources")
@Help("print lines matching a pattern")
public class GrepPlugin implements Plugin
{
   @DefaultCommand
   public void run(
         @PipeIn final InputStream pipeIn,
         @Option(name = "ignore-case", shortName = "i", help = "ignore case distinctions in both patterns and input", flagOnly = true)
         boolean ignoreCase,
         @Option(name = "regexp", shortName = "e", help = "match using a regular expression")
         String regExp,
         @Option(description = "PATTERN") String pattern,
         @Option(description = "FILE ...") Resource<?>[] resources,
         final PipeOut pipeOut
   ) throws IOException
   {
      Pattern matchPattern;
      if (regExp != null)
      {
         if (ignoreCase)
         {
            regExp = regExp.toLowerCase();
         }
         matchPattern = Pattern.compile(regExp);
      }
      else if (pattern == null)
      {
         throw new RuntimeException("you must specify a pattern");
      }
      else
      {
         if (ignoreCase)
         {
            pattern = pattern.toLowerCase();
         }
         matchPattern = Pattern.compile(".*" + pattern + ".*");
      }

      if (resources != null)
      {
         for (Resource<?> r : resources)
         {
            InputStream inputStream = r.getResourceInputStream();
            try
            {
               match(inputStream, matchPattern, pipeOut, ignoreCase);
            }
            finally
            {
               inputStream.close();
            }
         }
      }
      else if (pipeIn != null)
      {
         match(pipeIn, matchPattern, pipeOut, ignoreCase);
      }
      else
      {
         throw new RuntimeException("arguments required");
      }
   }

   private void match(InputStream instream, Pattern pattern, PipeOut out, boolean caseInsensitive) throws IOException
   {
      StringBuilder buf = new StringBuilder();

      int c;
      while ((c = instream.read()) != -1)
      {
         switch (c)
         {
         case '\r':
         case '\n':
            String s = caseInsensitive ? buf.toString().toLowerCase() : buf.toString();

            if (pattern.matcher(s).matches())
            {
               out.println(s);
            }
            buf = new StringBuilder();
            break;
         default:
            buf.append((char) c);
            break;
         }
      }
   }
}
