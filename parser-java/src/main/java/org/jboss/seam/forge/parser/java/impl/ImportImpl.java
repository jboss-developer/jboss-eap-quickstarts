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

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.jboss.seam.forge.parser.java.Import;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ImportImpl implements Import
{
   private JavaSource<?> parent;
   private AST ast = null;
   private CompilationUnit cu = null;
   private ImportDeclaration imprt = null;

   private void init(final JavaSource<?> parent)
   {
      this.parent = parent;
      cu = (CompilationUnit) parent.getInternal();
      ast = cu.getAST();
   }

   public ImportImpl(final JavaSource<?> parent)
   {
      init(parent);
      imprt = ast.newImportDeclaration();
   }

   public ImportImpl(final JavaSource<?> parent, final Object internal)
   {
      init(parent);
      imprt = (ImportDeclaration) internal;
   }

   @Override
   public String getName()
   {
      return imprt.getName().getFullyQualifiedName();
   }

   @Override
   public Import setName(final String name)
   {
      imprt.setName(ast.newName(Types.tokenizeClassName(name)));
      return this;
   }

   @Override
   public boolean isStatic()
   {
      return imprt.isStatic();
   }

   @Override
   public Import setStatic(final boolean value)
   {
      imprt.setStatic(value);
      return this;
   }

   @Override
   public JavaSource<?> getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public Object getInternal()
   {
      return imprt;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((imprt == null) ? 0 : imprt.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      ImportImpl other = (ImportImpl) obj;
      if (imprt == null)
      {
         if (other.imprt != null)
         {
            return false;
         }
      }
      else if (!imprt.equals(other.imprt))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "Import [" + getName() + "]";
   }
}
