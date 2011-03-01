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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Signals to the framework that the annotated class should be accessed using the given alias (given name).
 * <p/>
 * If two or more plugins share an alias, they must declare different {@link ResourceScope})s. The shell determines
 * which plugin to invoke when a {@link Resource} of the type requested by a {@link ResourceScope} is currently in
 * scope.
 * <p/>
 * Scopes and overloads are checked at boot time; if conflicts are detected, the shell will fail to boot. (No two
 * plugins or commands can declare the same {@link Alias} and {@link ResourceScope}). Similarly, no two facets may
 * declare the same alias.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock .
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Alias
{
   String value();
}
