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
package org.jboss.seam.sidekick.shell.util;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Patterns
{
   /**
    * A group of terms that are illegal to use as Java identifiers. For example:
    * "(if|package|public...while)" and so on.
    */
   public static final String JAVA_KEYWORDS = "abstract|" +
            "continue|" + "for|" + "new|" + "switch|" + "assert|" + "default|" +
            "if|" + "package|" + "synchronized|" + "boolean|" + "do|" + "goto|" +
            "private|" + "this|" + "break|" + "double|" + "implements|" + "protected|" +
            "throw|" + "byte|" + "else|" + "import|" + "public|" + "throws|" +
            "case|" + "enum|" + "instanceof|" + "return|" + "transient|" + "catch|" +
            "extends|" + "int|" + "short|" + "try|" + "char|" + "final|" +
            "interface|" + "static|" + "void|" + "class|" + "finally|" + "long|" +
            "strictfp|" + "volatile|" + "const|" + "float|" + "native|" + "super|" +
            "while";
}
