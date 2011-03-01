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
package org.jboss.seam.forge.shell.test.command;

import javax.inject.Singleton;

import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("motp")
@Singleton
public class MockOptionTestPlugin implements Plugin
{
   private String suppliedOption = "";
   private String requiredOption = "";
   private Boolean booleanOptionOmitted = null;

   @Command("suppliedOption")
   public void suppliedOption(@Option(name = "package",
         description = "Your java package",
         type = PromptType.JAVA_PACKAGE) final String option)
   {
      suppliedOption = option;
   }

   @Command("requiredOption")
   public void requiredOption(@Option(name = "package",
         required = true,
         description = "Your java package",
         type = PromptType.JAVA_PACKAGE) final String option)
   {
      requiredOption = option;
   }

   @Command("booleanOptionOmitted")
   public void booleanOptionOmitted(@Option(required = false,
         description = "Some boolean flag") final boolean option)
   {
      booleanOptionOmitted = option;
   }

   public String getSuppliedOption()
   {
      return suppliedOption;
   }

   public void setSuppliedOption(String suppliedOption)
   {
      this.suppliedOption = suppliedOption;
   }

   public String getRequiredOption()
   {
      return requiredOption;
   }

   public void setRequiredOption(String requiredOption)
   {
      this.requiredOption = requiredOption;
   }

   public Boolean getBooleanOptionOmitted()
   {
      return booleanOptionOmitted;
   }

   public void setBooleanOptionOmitted(Boolean booleanOptionOmitted)
   {
      this.booleanOptionOmitted = booleanOptionOmitted;
   }
}
