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

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.fshparser.FSHParser;
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.shell.util.ShellColor;
import org.mvel2.util.ParseTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.*;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
@Named("echo")
@Help("Writes input to output.")
@Topic("Shell Environment")
public class Echo implements Plugin
{
   @Inject
   Shell shell;

   @DefaultCommand
   public void run(
         @Option(help = "The text to be echoed") final String[] tokens,
         final PipeOut out)
   {
      if (tokens == null || tokens.length == 0)
      {
         return;
      }

      out.println(echo(shell, tokensToString(tokens)));
   }

   private static String tokensToString(String... tokens)
   {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < tokens.length; i++)
      {
         sb.append(tokens[i]);
         if (i + 1 < tokens.length)
         {
            sb.append(" ");
         }
      }

      return sb.toString();
   }


   private static String echo(Shell shell, String input)
   {
      char[] expr = input.toCharArray();
      StringBuilder out = new StringBuilder();
      int start = 0;
      int i = 0;
      for (; i < expr.length; i++)
      {
         if (i >= expr.length)
         {
            break;
         }

         switch (expr[i])
         {
         case '\'':
         case '"':
            out.append(new String(expr, start, i - start));
            start = i;
            i = ParseTools.balancedCapture(expr, i, expr[i]);
            out.append(new String(expr, start + 1, i - start - 1));
            start = ++i;
            break;
         case '$':
            out.append(new String(expr, start, i - start));
            start = ++i;
            while (i != expr.length && Character.isJavaIdentifierPart(expr[i]))
            {
               i++;
            }

            String var = new String(expr, start, i - start);
            if (shell.getProperties().containsKey(var))
            {
               out.append(String.valueOf(shell.getProperties().get(var)));
            }

            start = i;
            break;

         default:
            if (Character.isWhitespace(expr[i]))
            {
               out.append(new String(expr, start, i - start));
               while (i != expr.length && Character.isWhitespace(expr[i]))
               {
                  i++;
               }
               start = i;
            }
         }
      }

      if (i > start)
      {
         out.append(new String(expr, start, i - start));
      }

      return out.toString();
   }


}
