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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;

/**
 * Parses named varargs options such as:
 * <p>
 * <code>[command] {--option foo bar baz}</code>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class NamedValueVarargsOptionParser implements CommandParser
{

   @Override
   public Map<OptionMetadata, Object> parse(final CommandMetadata command, final Queue<String> tokens)
   {
      Map<OptionMetadata, Object> valueMap = new HashMap<OptionMetadata, Object>();
      String currentToken = tokens.peek();
      if (currentToken.startsWith("--"))
      {
         currentToken = currentToken.substring(2);
         if (command.hasOption(currentToken))
         {
            OptionMetadata option = command.getNamedOption(currentToken);
            if (option.isVarargs())
            {
               tokens.remove();
               List<String> args = new ArrayList<String>();
               while (!tokens.peek().startsWith("--"))
               {
                  args.add(tokens.remove());
               }
               valueMap.put(option, args.toArray()); // add the value, should we
                                                     // return this as a tuple
                                                     // instead?
            }
         }
      }
      return valueMap;
   }

}
