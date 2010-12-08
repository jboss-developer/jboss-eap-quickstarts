package org.jboss.seam.forge.grammar.java;

@SuppressWarnings
public enum MockInterface
{
   FOO, BAR, BAZ;
   
   private MockInterface()
   {
   }
   
   String getName()
   {
      return name();
   }
}
