package org.jboss.seam.forge.grammar.java;

import static org.jboss.seam.forge.test.grammar.java.common.MockEnum.FOO;

import java.net.URL;

import org.jboss.seam.forge.test.grammar.java.common.MockAnnotation;

@Deprecated
@SuppressWarnings("deprecation")
@SuppressWarnings(value="unchecked")
@MockAnnotation(FOO)
public class MockAnnotatedClass
{
   private String field;
   
   public MockAnnotatedClass()
   {
   }
}
