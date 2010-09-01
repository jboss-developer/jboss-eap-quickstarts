package org.jboss.seam.sidekick.shell.util;

import org.jboss.seam.sidekick.shell.cli.OptionMetadata;
import org.mvel2.util.ParseTools;
import org.mvel2.util.StringAppender;

/**
 * User: christopherbrock
 * Date: 31-Aug-2010
 * Time: 8:44:05 PM
 */
public final class ShellUtils
{
   private ShellUtils()
   {
   }

   public static String getOptionDescriptor(OptionMetadata optionMetaData)
   {
      StringAppender appender = new StringAppender("[");
      if (optionMetaData.isNamed())
      {
         appender.append(optionMetaData.getName()).append("=");
      }

      if (optionMetaData.getDescription().equals(""))
      {
         appender.append("<ARG").append(':').append(getTypeDescriptor(optionMetaData)).append(">");
      }
      else
      {
         appender.append('<').append(optionMetaData.getDescription()).append(':').append(optionMetaData.getType()).append('>');
      }

      return appender.append(']').toString();
   }


   public static String getTypeDescriptor(OptionMetadata optionMetaData)
   {
      return getTypeDescriptor(optionMetaData.getType());
   }


   public static String getTypeDescriptor(Class<?> type)
   {
      if (ParseTools.boxPrimitive(type).isAssignableFrom(Number.class))
      {
         return "numeric";
      }
      else
      {
         return type.getCanonicalName();
      }
   }
}
