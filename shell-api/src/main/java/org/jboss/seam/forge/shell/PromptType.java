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

package org.jboss.seam.forge.shell;

import org.jboss.seam.forge.shell.util.Patterns;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public enum PromptType
{
   ANY(".*", String.class),
   JAVA_PACKAGE("(?i)(~\\.)?([a-z0-9_]+\\.?)+[a-z0-9_]", String.class),
   JAVA_VARIABLE_NAME("^(?!(" + Patterns.JAVA_KEYWORDS + "))[A-Za-z0-9$_]+$", String.class),
   JAVA_CLASS(JAVA_PACKAGE.pattern + "\\.?[a-z0-9$_]", String.class);

   private final String pattern;
   private final Class<?> type;

   private PromptType(final String pattern, final Class<?> type)
   {
      this.pattern = pattern;
      this.type = type;
   }

   public String getPattern()
   {
      return pattern;
   }

   /**
    * The {@link Class} type to which this prompt should be converted.
    */
   public Class<?> getConversionType()
   {
      return type;
   }

}