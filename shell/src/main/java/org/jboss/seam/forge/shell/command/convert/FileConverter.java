package org.jboss.seam.forge.shell.command.convert;

import java.io.File;

import org.mvel2.ConversionHandler;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FileConverter implements ConversionHandler
{
   @Override
   public Object convertFrom(Object in)
   {
      return new File(in.toString());
   }

   @Override
   public boolean canConvertFrom(Class type)
   {
      return String.class.isAssignableFrom(type);
   }
}
