package org.jboss.seam.forge.parser.java.util;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class Assert
{
   public static void notNull(Object object, String message) throws IllegalStateException
   {
      if (object == null)
      {
         throw new IllegalStateException(message);
      }
   }
}
