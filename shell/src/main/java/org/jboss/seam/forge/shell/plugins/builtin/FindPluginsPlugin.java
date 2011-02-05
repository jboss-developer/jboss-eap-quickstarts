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

package org.jboss.seam.forge.shell.plugins.builtin;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.util.GeneralUtils;
import org.jboss.seam.forge.shell.util.PluginRef;
import org.jboss.seam.forge.shell.util.PluginRepoUtil;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Mike Brock .
 */
@Named("find-plugins")
public class FindPluginsPlugin implements Plugin
{

   @DefaultCommand
   public void find(@Option(description = "search-string") String searchString) throws Exception
   {
      List<PluginRef> pluginList = PluginRepoUtil.findPlugin("http://seamframework.org/service/File/148617", searchString);

      for (PluginRef ref : pluginList)
      {
         System.out.println(" - " + ref.getName() + " (" + ref.getArtifact() + ")");
      }
   }
}
