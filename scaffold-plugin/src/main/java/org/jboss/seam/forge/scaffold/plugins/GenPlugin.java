/*
 * JBoss, by Red Hat.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.seam.forge.scaffold.plugins;

import org.jboss.fpak.GenerationContext;
import org.jboss.fpak.model.Definition;
import org.jboss.fpak.strategy.ParseStrategy;
import org.jboss.fpak.strategy.RunStrategy;
import org.jboss.fpak.strategy.builtin.DefaultParseStrategy;
import org.jboss.fpak.strategy.builtin.DefaultRunStrategy;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;

/**
 * @author Mike Brock .
 */
@Named("gen")
public class GenPlugin implements Plugin
{
   private static final String[] DEFAULT_SEARCH_PATHS
         = {"org/jboss/seam/forge/scaffold/templates/fpak/", "~/.forge/plugins/scaffold/templates/fpak/"};

   private final Shell shell;

   @Inject
   public GenPlugin(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void gen(
         PipeOut out,
         @Option(description = "profile", required = true) String profile,
         @Option(description = "args...") String... args)
   {

      InputStream profileStream = findProfile(profile);

      if (profileStream == null)
      {
         out.println("no such profile: " + profile);
         return;
      }

      GenerationContext ctx = new GenerationContext();
      ctx.setWorkingDirectory(shell.getCurrentDirectory().getUnderlyingResourceObject());
      ctx.addInputStream(profileStream);
      ctx.setTemplateArgs(args);
      ctx.getGlobals().put("_out", out);

      ParseStrategy parseStrategy = new DefaultParseStrategy();
      Definition def = parseStrategy.doStrategy(ctx);

      RunStrategy runStrategy = new DefaultRunStrategy();
      runStrategy.doStrategy(ctx, def);
   }

   private static InputStream findProfile(String name)
   {
      ClassLoader cl = GenPlugin.class.getClassLoader();

      InputStream inStream;
      for (String path : DEFAULT_SEARCH_PATHS)
      {
         if ((inStream = cl.getResourceAsStream(path + name + ".fpk")) != null)
         {
            return inStream;
         }
      }


      return null;
   }
}
