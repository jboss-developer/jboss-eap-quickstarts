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
package org.jboss.seam.forge.shell.plugins;

/**
 * A custom {@link Plugin} must implement this interface in order to be detected and installed at framework boot-time.
 * In order to create plugin shell-commands, one must create a method annotated with @{@link Command}. Any command
 * method parameters to be provided as input through the shell must be individually annotated with the @{@link Option}
 * annotation; other (non-annotated) command parameters are ignored.
 * <p/>
 * In order to control the name of a custom plugin, the {@link Alias} annotation may be added to any {@link Plugin}
 * type.
 * <p/>
 * Plugin types may be annotated with any of the following constraints in order to ensure proper dependencies are
 * satisfied at runtime: {@link RequiresFacet}, {@link RequiresPackagingType}, {@link RequiresResource}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Plugin
{

}
