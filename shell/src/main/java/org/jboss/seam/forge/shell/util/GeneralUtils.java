package org.jboss.seam.forge.shell.util;

import org.jboss.seam.forge.shell.Shell;

import java.util.*;

public class GeneralUtils
{
   public static <T> List<T> concatArraysToList(T[]... arrays)
   {
      List<T> newList = new ArrayList<T>();
      for (T[] elArray : arrays)
      {
         newList.addAll(Arrays.asList(elArray));
      }

      return newList;
   }

   public static String elementListSimpleTypesToString(List<Class> list)
   {
      StringBuilder sbuild = new StringBuilder();
      for (int i = 0; i < list.size(); i++)
      {
         sbuild.append(list.get(0).getSimpleName());
         if (i < list.size()) sbuild.append(", ");
      }
      return sbuild.toString();
   }

   public static String elementSetSimpleTypesToString(Set<Class> set)
   {
      StringBuilder sbuild = new StringBuilder();

      for (Iterator<Class> iter = set.iterator(); iter.hasNext();)
      {
         sbuild.append(iter.next().getSimpleName());
         if (iter.hasNext()) sbuild.append(", ");
      }
      return sbuild.toString();
   }


   public static void printOutColumns(List<String> list, Shell shell, boolean sort)
   {

      int width = shell.getWidth();
      int maxLength = 0;

      for (String s : list)
      {
         if (s.length() > maxLength) maxLength = s.length();
      }

      if (sort) {
         Collections.sort(list);
      }

      int cols = width / (maxLength + 4);
      int colSize = width / cols;

      if (cols == 0)
      {
         colSize = width;
         cols = 1;
      }

      int i = 0;
      for (String s : list)
      {
         shell.print(s);
         shell.print(pad(colSize - s.length()));
         if (++i == cols)
         {
            shell.println();
            i = 0;
         }
      }
      shell.println();

   }

   public static String pad(final int amount)
   {
      char[] padding = new char[amount];
      for (int i = 0; i < amount; i++)
      {
         padding[i] = ' ';
      }
      return new String(padding);
   }

}
