package org.jboss.seam.forge.grammar.java;

import org.jboss.seam.forge.test.grammar.java.common.MockAnnotation;

import static org.jboss.seam.forge.test.grammar.java.common.MockEnum.FOO;

public class MockAnnotatedMethod
{
   @Deprecated
   @SuppressWarnings("deprecation")
   @SuppressWarnings(value = "unchecked")
   @MockAnnotation(FOO)
   public MockAnnotatedMethod()
   {
   }
}
