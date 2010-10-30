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
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.jboss.seam.forge.shell.PromptType;

/**
 * A command option.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Qualifier
@Target({ METHOD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface Option
{
   /**
    * The name of this option.
    */
   String name() default "";


   /**
    * An optional short version of the flag name.
    * @return
    */
   String shortName() default "";


   String description() default "";

   /**
    * Sets whether or not the option is just a flag. Option must be a boolean in this case.
    */
   boolean flagOnly() default false;

   /**
    * Specify whether or not this option is required.
    */
   boolean required() default false;

   /**
    * The default value for this option, if not provided in user input.
    */
   String defaultValue() default "";

   /**
    * Help text for this option.
    */
   String help() default "";

   /**
    * The prompt type to use when validating user input. This should be used
    * carefully!
    * <p>
    * <b>**WARNING**</b> Since specifying a {@link PromptType} restricts user
    * input, you need to make sure that the option type is compatible with this
    * input, or exceptions may occur. (String or Object are your safest
    * choices.)
    */
   PromptType type() default PromptType.ANY;
}
