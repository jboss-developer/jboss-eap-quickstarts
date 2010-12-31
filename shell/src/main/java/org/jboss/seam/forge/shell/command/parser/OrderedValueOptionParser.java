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

import java.util.Queue;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.exceptions.CommandParserException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class OrderedValueOptionParser implements CommandParser
{

   @Override
   public CommandParserContext parse(final CommandMetadata command, final Queue<String> tokens,
            final CommandParserContext ctx)
   {
      int numberOrderedParams = ctx.getNumberOrderedParams();
      try
      {
         String currentToken = tokens.peek();
         if (!currentToken.startsWith("--"))
         {
            OptionMetadata option = command.getOrderedOptionByIndex(ctx.getParmCount());
            if (!option.isVarargs())
            {
               ctx.put(option, currentToken); // add the value, should we
               // return this as a tuple
               // instead?
               tokens.remove();

               ctx.incrementParmCount();
            }
         }
      }
      catch (IllegalArgumentException e)
      {
         throw new CommandParserException(command, "The command " + command.getName() + " takes ["
                  + numberOrderedParams + "] argument(s).");
      }
      return ctx;
   }

}
