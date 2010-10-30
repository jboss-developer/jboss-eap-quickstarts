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
package org.jboss.seam.forge.shell.command.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;

/**
 * Parses named boolean options such as:
 * <p/>
 * <code>[command] {--toggle}</code>
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class NamedBooleanOptionParser implements CommandParser
{

   @Override
   public Map<OptionMetadata, Object> parse(final CommandMetadata command, final Queue<String> tokens)
   {
      Map<OptionMetadata, Object> valueMap = new HashMap<OptionMetadata, Object>();
      String currentToken = tokens.peek();
      if (currentToken.matches("--?\\S+"))
      {
         if (currentToken.length() > 1 && currentToken.charAt(1) != '-')
         {
            for (int i = 1; i < currentToken.length(); i++)
            {
               String shortOption = currentToken.substring(i, i + 1);

               if (command.hasShortOption(shortOption))
               {
                  processOption(valueMap, tokens, command, shortOption);
               }
               else {
                  throw new RuntimeException("unknown option: " + shortOption);
               }
            }

            tokens.remove(); // increment the chain of tokens
         }
         else
         {
            currentToken = currentToken.substring(2);

            if (command.hasOption(currentToken))
            {
               processOption(valueMap, tokens, command, currentToken);
            }
         }
      }
      return valueMap;
   }

   private static void processOption(Map<OptionMetadata, Object> valueMap, Queue<String> tokens,
                                           CommandMetadata command, String currentToken)
   {
      OptionMetadata option = command.getNamedOption(currentToken);

      if (option.isBoolean())
      {
         String value = "true";
         if (!option.isFlagOnly() && !tokens.isEmpty())
         {
            String nextToken = tokens.peek();
            if (nextToken.matches("true|false"))
            {
               value = nextToken;
            }
         }

         valueMap.put(option, value);
      }
   }


}
