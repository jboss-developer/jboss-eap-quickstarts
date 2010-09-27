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

package org.jboss.seam.sidekick.shell.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.seam.sidekick.shell.PromptType;
import org.jboss.seam.sidekick.shell.util.Patterns;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PromptTypeTest
{
   @Test
   public void testJavaPackage() throws Exception
   {
      assertTrue("org".matches(PromptType.JAVA_PACKAGE.getPattern()));
      assertFalse("org.".matches(PromptType.JAVA_PACKAGE.getPattern()));
      assertTrue("org.jboss".matches(PromptType.JAVA_PACKAGE.getPattern()));
      assertFalse("org.jboss.".matches(PromptType.JAVA_PACKAGE.getPattern()));
      assertTrue("org.jboss_project".matches(PromptType.JAVA_PACKAGE.getPattern()));
      assertFalse("org.jboss_$f00".matches(PromptType.JAVA_PACKAGE.getPattern()));
   }

   @Test
   public void testJavaVariableName() throws Exception
   {
      assertTrue("gamesPlayed".matches(PromptType.JAVA_VARIABLE_NAME.getPattern()));
      assertFalse("(*#$%".matches(PromptType.JAVA_VARIABLE_NAME.getPattern()));
      assertFalse("public".matches(PromptType.JAVA_VARIABLE_NAME.getPattern()));
   }

   @Test
   public void testPatternsReservedWords() throws Exception
   {
      assertTrue("for".matches(Patterns.JAVA_KEYWORDS));
      assertTrue("private".matches(Patterns.JAVA_KEYWORDS));
      assertTrue("package".matches(Patterns.JAVA_KEYWORDS));
      assertTrue("class".matches(Patterns.JAVA_KEYWORDS));
      assertTrue("while".matches(Patterns.JAVA_KEYWORDS));
      assertTrue("volatile".matches(Patterns.JAVA_KEYWORDS));
   }

}
