package org.jboss.seam.forge.project.util;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.util.pathspec.PathspecParser;

import java.io.File;

/**
 * A set of utilities to work with the resources API.
 *
 * @author Mike Brock
 */
public class ResourceUtil
{
   /**
    * A simple utility method to locate the outermost contextual File reference for the specified resource.
    *
    * @param r resource instance.
    * @return outermost relevant file context.
    */
   public static File getContextFile(Resource r)
   {
      do
      {
         Object o = r.getUnderlyingResourceObject();
         if (o instanceof File)
         {
            return (File) r.getUnderlyingResourceObject();
         }

      }
      while ((r = r.getParent()) != null);

      return null;
   }

   public static File getContextDirectory(final Resource r)
   {
      final File ctx = getContextFile(r);
      if (ctx != null && !ctx.isDirectory())
      {
         return ctx.getParentFile();
      }
      return ctx;
   }

   public static Resource<?> parsePathspec(final Resource<?> resource, final String pathspec) {
     return new PathspecParser(resource, pathspec).parse();
   }

}
