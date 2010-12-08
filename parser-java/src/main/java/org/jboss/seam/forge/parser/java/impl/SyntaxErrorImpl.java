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
package org.jboss.seam.forge.parser.java.impl;

import org.eclipse.jdt.core.compiler.IProblem;
import org.jboss.seam.forge.parser.Internal;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.SyntaxError;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class SyntaxErrorImpl implements SyntaxError, Internal
{
   private final JavaSource<?> parent;
   private final IProblem problem;

   public SyntaxErrorImpl(final JavaSource<?> parent, final Object internal)
   {
      this.parent = parent;
      this.problem = (IProblem) internal;
   }

   @Override
   public String getDescription()
   {
      int line = problem.getSourceLineNumber();
      int begin = problem.getSourceStart();
      int end = problem.getSourceEnd();

      String snippit = parent.toString().substring(begin, end);

      String message = "Line " + line + " near <" + begin + "," + end + ">: \"" + snippit + "\" - "
               + problem.getMessage();

      return message;
   }

   @Override
   public Object getInternal()
   {
      return problem;
   }

   @Override
   public String toString()
   {
      return getDescription();
   }

}
