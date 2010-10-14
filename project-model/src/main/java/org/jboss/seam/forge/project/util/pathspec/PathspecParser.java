package org.jboss.seam.forge.project.util.pathspec;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;

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
      String tk;

      if (path.startsWith("~"))
      {
         File homeDir = new File(System.getProperty("user.home")).getAbsoluteFile();

         if (path.length() == 1)
         {
            return new DirectoryResource(homeDir);
         }
         else
         {
            path = path.substring(1);
            r = new DirectoryResource(homeDir);
         }
      }

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

         default:
            if (read() == '.') continue;
            cursor--;
            tk = capture();

            if (tk.startsWith("/"))
            {
               if (res == r)
               {
                  r = new DirectoryResource(new File(tk));
                  cursor++;
                  continue;
               }
               else
               {
                  tk = tk.substring(1);
               }
            }

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

      //capture can start with a '/'
      if (path.charAt(cursor) == '/') cursor++;

      while (cursor != length && path.charAt(cursor) != '/') cursor++;
      return path.substring(start, cursor);
   }
}
