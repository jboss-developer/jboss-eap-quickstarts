package org.jboss.seam.forge.project.util.pathspec;

import org.jboss.seam.forge.project.Resource;

import java.io.File;

public class PathspecParser
{
   private int cursor;
   private int length;

   private Resource<?> res;
   private String path;

   public PathspecParser(Resource<?> res, String path)
   {
      this.res = res;

      this.length = (
            this.path = path
      ).length();
   }

   public Resource<?> parse()
   {
      Resource r = res;

      while (cursor < length)
      {
         switch (path.charAt(cursor++))
         {
         case '.':
            if (read() == '.')
            {
               Resource parent = r.getParent();
               if (parent == null) return r;
               r = parent;
            }
            break;

         case '/':
            if (read() == '.') continue;

            String tk = capture();
            Resource child = r.getChild(tk);
            if (child == null)
            {
               throw new RuntimeException("no such child: " + child);
            }
            r = child;
            break;
         }
      }

      return r;
   }

   private char read()
   {
      if (cursor != length)
      {
         return path.charAt(cursor);
      }
      return (char) 0;
   }

   private String capture()
   {
      int start = cursor;
      while (cursor != length && path.charAt(cursor) != '/') cursor++;
      return path.substring(start, cursor);
   }
}
