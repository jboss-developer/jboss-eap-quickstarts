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
package org.jboss.seam.sidekick.test.grammar.java.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.seam.sidekick.parser.java.VisibilityScoped;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class VisibilityTest
{
   private VisibilityScoped<?> target;

   public void setTarget(VisibilityScoped<?> target)
   {
      this.target = target;
   }

   @Before
   public void reset()
   {
      resetTests();
   }

   public abstract void resetTests();

   @Test
   public void testSetPublic() throws Exception
   {
      target.setPublic();
      assertTrue(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertFalse(target.isProtected());
   }

   @Test
   public void testSetPrivate() throws Exception
   {
      target.setPrivate();
      assertFalse(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertTrue(target.isPrivate());
      assertFalse(target.isProtected());
   }

   @Test
   public void testSetProtected() throws Exception
   {
      target.setProtected();
      assertFalse(target.isPublic());
      assertFalse(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertTrue(target.isProtected());
   }

   @Test
   public void testSetPackagePrivate() throws Exception
   {
      target.setPackagePrivate();
      assertFalse(target.isPublic());
      assertTrue(target.isPackagePrivate());
      assertFalse(target.isPrivate());
      assertFalse(target.isProtected());
   }
}
