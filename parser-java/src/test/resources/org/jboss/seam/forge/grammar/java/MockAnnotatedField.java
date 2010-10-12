package org.jboss.seam.forge.grammar.java;

import org.jboss.seam.forge.test.grammar.java.common.MockAnnotation;
import org.jboss.seam.forge.test.grammar.java.common.MockEnum;

import static org.jboss.seam.forge.test.grammar.java.common.MockEnum.*;
import org.junit.experimental.categories.Category;

import 

public class MockAnnotatedField
{
   @Deprecated
   @SuppressWarnings("deprecation")
   @SuppressWarnings(value="unchecked")
   @MockAnnotation(FOO)
   private String field;
}
