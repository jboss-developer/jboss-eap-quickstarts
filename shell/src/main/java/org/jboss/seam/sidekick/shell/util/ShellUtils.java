package org.jboss.seam.sidekick.shell.util;

import org.jboss.seam.sidekick.shell.command.OptionMetadata;
import org.mvel2.util.ParseTools;
import org.mvel2.util.StringAppender;

import static org.mvel2.util.ParseTools.boxPrimitive;

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
         appender.append("ARG").append(" (").append(getTypeDescriptor(optionMetaData)).append(")");
      }
      else
      {
         appender.append(optionMetaData.getDescription()).append(" (").append(getTypeDescriptor(optionMetaData)).append(")");
      }

      return appender.append(']').toString();
   }


   public static String getTypeDescriptor(OptionMetadata optionMetaData)
   {
      return getTypeDescriptor(optionMetaData.getType());
   }


   public static String getTypeDescriptor(Class<?> type)
   {
      if (Number.class.isAssignableFrom(boxPrimitive(type)))
      {
         return "numeric";
      }
      else
      {
         return type.getCanonicalName();
      }
   }
}
