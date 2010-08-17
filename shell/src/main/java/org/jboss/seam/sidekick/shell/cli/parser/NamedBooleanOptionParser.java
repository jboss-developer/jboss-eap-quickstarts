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
package org.jboss.seam.sidekick.shell.cli.parser;

import java.util.Map;
import java.util.Queue;

import org.jboss.seam.sidekick.shell.cli.CommandMetadata;
import org.jboss.seam.sidekick.shell.cli.OptionMetadata;

/**
 * Parses named boolean options such as:
 * <p>
 * <code>[command] {--toggle}</code>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class NamedBooleanOptionParser implements CommandParser
{

   @Override
   public void parse(final CommandMetadata command, final Map<OptionMetadata, Object> valueMap,
            final Queue<String> tokens)
   {
      String currentToken = tokens.peek();
      if (currentToken.startsWith("--"))
      {
         currentToken = currentToken.substring(2);
         OptionMetadata option = command.getNamedOption(currentToken);

         if (option.isBoolean())
         {
            tokens.remove();
            String value = "true";
            if (!tokens.isEmpty())
            {
               String nextToken = tokens.peek();
               if (nextToken.matches("true|false"))
               {
                  value = nextToken;
                  tokens.remove(); // increment the chain of tokens
               }
            }
            valueMap.put(option, value); // add the value, should we return this
                                         // as a tuple instead?
         }
      }
   }

}
