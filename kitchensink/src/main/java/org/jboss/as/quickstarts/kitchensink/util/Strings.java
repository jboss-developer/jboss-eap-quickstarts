package org.jboss.as.quickstarts.kitchensink.util;

import javax.inject.Named;

@Named
public class Strings {
   
   public String truncate(String original, int maxLength) {
      return truncate(original, maxLength, false);
   }
   
   public String truncate(String original, int maxLength, boolean showEnd) {
      if (original == null) {
         throw new IllegalArgumentException("original must not be null");
      } else if (original.length() <= maxLength) {
         return original;
      } else {
         if (showEnd) {
            return "... " + original.substring(original.length() - maxLength + 4);
         } else {
            return original.substring(0, (maxLength - 4)) + " ...";
         }
      }
   }
   
   @Deprecated
   // TODO remove this hack!
   public String concat(String string1, String string2, String string3) {
      return string1 + string2 + string3;
   }
   
}
