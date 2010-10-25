package org.jboss.seam.forge.project;

public @interface AppliesTo
{
   Class<? extends Resource<?>>[] value();
}
