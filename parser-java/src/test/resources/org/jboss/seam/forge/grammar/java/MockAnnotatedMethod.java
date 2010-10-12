package org.jboss.seam.forge.grammar.java;

import static org.jboss.seam.forge.test.grammar.java.common.MockEnum.FOO;

import org.jboss.seam.forge.test.grammar.java.common.MockAnnotation;

public class MockAnnotatedMethod
{
   @Deprecated
   @SuppressWarnings("deprecation")
   @SuppressWarnings(value="unchecked")
   @MockAnnotation(FOO)
   public MockAnnotatedMethod()
   {
   }
}
