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

package org.jboss.seam.forge.shell;


/**
 * @author Mike Brock .
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ShellPrintWriter
{
   /**
    * Write the given bytes to the console.
    */
   public void write(byte b);

   /**
    * Print output to the console.
    */
   void print(String output);

   /**
    * Print output to the console, followed by the newline character.
    */
   void println(String output);

   /**
    * Print a blank line to the console.
    */
   void println();

   /**
    * Print color output to the console.
    */
   void print(ShellColor color, String output);

   /**
    * Print color output to the console, followed by the newline character.
    */
   void println(ShellColor color, String output);

   /**
    * Render a color for the current terminal emulation by encapsulating the string is the appropriate escape codes
    */
   public String renderColor(ShellColor color, String output);
}
