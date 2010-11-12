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

package org.jboss.seam.forge.parser.java;

import org.jboss.seam.forge.parser.java.util.Assert;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public enum Visibility
{
   PUBLIC("public"),
   PROTECTED("protected"),
   PRIVATE("private"),
   PACKAGE_PRIVATE("package private");

   private Visibility(String scope)
   {
      this.scope = scope;
   }

   private String scope;

   public String scope()
   {
      return scope;
   }

   public static Visibility getFrom(VisibilityScoped<?> target)
   {
      Assert.notNull(target, "VisibilityScoped<T> target must not be null.");

      if (target.isPackagePrivate()) return PACKAGE_PRIVATE;
      if (target.isPrivate()) return PRIVATE;
      if (target.isPublic()) return PUBLIC;
      if (target.isProtected())
         return PROTECTED;

      else
      {
         throw new IllegalStateException(VisibilityScoped.class.getSimpleName() + " target does not comply with visibility scoping. Must be one of " + Visibility.values() + "[ " + target + "]");
      }
   }

   @Override
   public String toString()
   {
      return scope;
   }

   public static <T extends VisibilityScoped<?>> T set(T target, Visibility scope)
   {
      Assert.notNull(target, "VisibilityScoped<T> target must not be null.");
      Assert.notNull(scope, "Visibility scope must not be null");

      if (PRIVATE.equals(scope)) target.setPrivate();
      if (PACKAGE_PRIVATE.equals(scope)) target.setPackagePrivate();
      if (PROTECTED.equals(scope)) target.setProtected();
      if (PUBLIC.equals(scope))
         target.setPublic();

      else
      {
         throw new IllegalStateException("Unknown Visibility scope.");
      }

      return target;
   }
}
