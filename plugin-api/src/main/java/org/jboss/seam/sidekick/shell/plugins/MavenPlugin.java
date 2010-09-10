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
package org.jboss.seam.sidekick.shell.plugins;

import java.util.List;

import org.apache.maven.model.Dependency;

/**
 * A plugin that depends on Apache Maven for dependency management and
 * resolution.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MavenPlugin extends InstallablePlugin
{
   /**
    * Get a list of the Maven dependencies required by this plugin.
    */
   public List<Dependency> getDependencies();

   /**
    * Get a list of the Maven POM packaging types this plugin is compatible
    * with; at least one of these types must be used in order for this plugin to
    * function. Returning an empty list signals that this plugin is compatible
    * with all packaging types.
    */
   public List<PackagingType> getCompatiblePackagingTypes();
}
