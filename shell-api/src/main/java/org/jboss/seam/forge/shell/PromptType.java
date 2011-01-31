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

import org.jboss.seam.forge.shell.util.Patterns;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public enum PromptType
{
   ANY(".*"),
   DEPENDENCY_ID("[^:]+:[^:]+:?([^:]+:?){0,3}"),
   JAVA_PACKAGE("(?i)(~\\.)?([a-z0-9_]+\\.?)+[a-z0-9_]"),
   JAVA_VARIABLE_NAME("^(?!(" + Patterns.JAVA_KEYWORDS + "))[A-Za-z0-9$_]+$"),
   JAVA_CLASS(JAVA_PACKAGE.pattern + "\\.?[a-z0-9$_]"),
   FILE_PATH(".*");

   private final String pattern;

   private PromptType(final String pattern)
   {
      this.pattern = pattern;
   }

   public String getPattern()
   {
      return pattern;
   }

}