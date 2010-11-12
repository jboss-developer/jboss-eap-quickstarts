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

package org.jboss.seam.forge.shell.completer;

import jline.console.completer.Completer;

import java.util.List;

/**
 * Adapts the shell's {@link CommandCompleter} to JLine's {@link Completer}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CommandCompleterAdaptor implements Completer, CommandCompleter
{
   private final CommandCompleter completer;

   public CommandCompleterAdaptor(final CommandCompleter completer)
   {
      this.completer = completer;
   }

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      if (completer != null)
      {
         return completer.complete(buffer, cursor, candidates);
      }
      return -1;
   }
}
