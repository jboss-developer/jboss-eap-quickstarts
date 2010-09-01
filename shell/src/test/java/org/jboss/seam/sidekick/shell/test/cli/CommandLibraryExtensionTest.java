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
package org.jboss.seam.sidekick.shell.test.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.seam.sidekick.shell.cli.CommandLibraryExtension;
import org.jboss.seam.sidekick.shell.cli.CommandMetadata;
import org.jboss.seam.sidekick.shell.cli.OptionMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginMetadata;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CommandLibraryExtensionTest
{
   CommandLibraryExtension library = new CommandLibraryExtension();
   private final PluginMetadata plugin = library.getMetadataFor(MockNamedPlugin.class);

   @Test
   public void testParsePluginWithoutHelp() throws Exception
   {
      assertEquals("mnp", plugin.getName());
      assertEquals("", plugin.getHelp());

      assertTrue(Plugin.class.isAssignableFrom(plugin.getType()));
   }

   @Test
   public void testParsePluginWithDefaultAndNormalCommands() throws Exception
   {
      assertEquals(6, plugin.getCommands().size());
      assertTrue(plugin.hasDefaultCommand());
      assertNotNull(plugin.getDefaultCommand());

      assertTrue(Plugin.class.isAssignableFrom(plugin.getType()));
   }

   @Test
   public void testParseDefaultCommand() throws Exception
   {
      CommandMetadata defaultCommand = plugin.getDefaultCommand();

      assertEquals(plugin.getName(), defaultCommand.getName());
      assertEquals("This is a mock default command", defaultCommand.getHelp());
   }

   @Test
   public void testParseNormalCommand() throws Exception
   {
      CommandMetadata normal = plugin.getCommand("normal");

      assertEquals("normal", normal.getName());
      assertEquals("", normal.getHelp());
   }

   @Test
   public void testParseHelplessCommand() throws Exception
   {
      CommandMetadata normal = plugin.getCommand("helpless");
      assertEquals("helpless", normal.getName());
      assertEquals("", normal.getHelp());
   }

   @Test
   public void testParseOptions() throws Exception
   {
      CommandMetadata command = plugin.getCommand("normal");

      List<OptionMetadata> options = command.getOptions();

      assertEquals(1, options.size());
      assertEquals("", options.get(0).getName());
      assertEquals("THE OPTION", options.get(0).getDescription());
   }

   @Test
   public void testParseNamedOption() throws Exception
   {
      CommandMetadata command = plugin.getCommand("named");

      List<OptionMetadata> options = command.getOptions();
      OptionMetadata option = options.get(0);

      assertEquals(1, options.size());
      assertEquals("named", option.getName());
      assertEquals("", option.getDescription());
      assertEquals("true", option.getDefaultValue());
   }
}
