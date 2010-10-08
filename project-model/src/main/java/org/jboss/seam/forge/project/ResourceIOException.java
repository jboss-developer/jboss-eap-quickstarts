package org.jboss.seam.forge.project;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class ResourceIOException extends RuntimeException
{
   public ResourceIOException()
   {
   }

   public ResourceIOException(String message)
   {
      super(message);
   }

   public ResourceIOException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public ResourceIOException(Throwable cause)
   {
      super(cause);
   }
}
