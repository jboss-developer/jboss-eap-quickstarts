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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import org.jboss.seam.forge.shell.command.CommandMetadata;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class CompositeCommandParser implements CommandParser
{
   List<CommandParser> parsers = new ArrayList<CommandParser>();

   public CompositeCommandParser(final CommandParser... parsers)
   {
      this.parsers = Arrays.asList(parsers);
   }

   @Override
   public CommandParserContext parse(final CommandMetadata command, final Queue<String> tokens,
            final CommandParserContext ctx)
   {
      boolean complete = false;
      CommandParserContext context = ctx;
      while (!complete)
      {
         boolean altered = false;
         for (CommandParser parser : parsers)
         {
            if (tokens.size() == 0)
            {
               complete = true;
               break;
            }

            int size = tokens.size();
            context = parser.parse(command, tokens, context);

            if (size > tokens.size())
            {
               altered = true;
               break;
            }
         }

         if (!altered)
         {
            break;
         }
      }
      return ctx;
   }

}
