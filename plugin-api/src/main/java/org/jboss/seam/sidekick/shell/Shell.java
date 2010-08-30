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

package org.jboss.seam.sidekick.shell;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Shell
{
   /**
    * Return true if this shell is currently running in pretend mode.
    * <p>
    * Modifications to files made while running in pretend mode are made in a temporary directory, and the output is
    * produced as a Diff that can then be applied to the project.
    */
   public boolean isPretend();

   /**
    * Return true if this shell is currently running in verbose mode.
    */
   public boolean isVerbose();

   /**
    * Clear the console.
    */
   void clear();

   /**
    * Prompt for user input, and return as a String.
    */
   String prompt();

   /**
    * Prompt for user input, first printing the given line, then return user input as a String.
    */
   String prompt(String prompt);

   /**
    * Prompt for boolean user input (Y/n), first printing the given line, then returning user input as a boolean. The
    * value returned will default to <code>true</code> if an empty or whitespace-only user input is read.
    */
   boolean promptBoolean(String string);

   /**
    * 
    * Prompt for boolean user input (Y/n), first printing the given line, then returning user input as a boolean.
    * 
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    */
   boolean promptBoolean(String message, boolean defaultIfEmpty);

   /**
    * Write output to the console.
    */
   void write(String output);

   void print(String output);

   void println(String output);

   void println();

   /**
    * Write output to the console, only if {@link #isVerbose()}<code> == true</code>.
    */
   void writeVerbose(String output);

}
