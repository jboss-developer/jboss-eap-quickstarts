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

package org.jboss.seam.forge.shell.command.parser;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;

import java.util.Queue;

/**
 * Parses named boolean options such as:
 * <p/>
 * <code>[command] {--toggle}</code>
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
public class NamedBooleanOptionParser implements CommandParser
{

   @Override
   public CommandParserContext parse(final CommandMetadata command, final Queue<String> tokens,
                                     final CommandParserContext ctx)
   {
      String currentToken = tokens.peek();
      if (currentToken.matches("--?\\S+"))
      {
         if ((currentToken.length() > 1) && (currentToken.charAt(1) != '-'))
         {
            for (int i = 1; i < currentToken.length(); i++)
            {
               String shortOption = currentToken.substring(i, i + 1);

               if (command.hasShortOption(shortOption))
               {
                  processOption(ctx, tokens, command, shortOption, true);
               }
               else
               {
                  throw new RuntimeException("unknown option: " + shortOption);
               }
            }
            tokens.remove();
         }
         else
         {
            currentToken = currentToken.substring(2);

            if (command.hasOption(currentToken))
            {
               processOption(ctx, tokens, command, currentToken, false);
            }
         }
      }
      return ctx;
   }

   private static void processOption(final CommandParserContext ctx, final Queue<String> tokens,
                                     final CommandMetadata command, final String currentToken,
                                     final boolean shortOption)
   {
      OptionMetadata option = command.getNamedOption(currentToken);

      if (option.isBoolean())
      {
         String value = "true";
         if (!tokens.isEmpty())
         {
            if (!shortOption)
            {
               tokens.remove();
            }
            String nextToken = tokens.peek();
            if (!option.isFlagOnly() && (nextToken != null) && nextToken.matches("true|false"))
            {
               value = nextToken;
               tokens.remove();
            }
         }

         ctx.put(option, value);
      }
   }
}
