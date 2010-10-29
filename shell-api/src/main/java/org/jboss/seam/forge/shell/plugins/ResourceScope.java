package org.jboss.seam.forge.shell.plugins;

import org.jboss.seam.forge.project.Resource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface ResourceScope
{
    Class<? extends Resource<?>>[] value();
}
