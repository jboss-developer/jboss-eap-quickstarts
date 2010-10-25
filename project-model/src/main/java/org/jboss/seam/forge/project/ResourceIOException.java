package org.jboss.seam.forge.project;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class ResourceIOException extends RuntimeException
{
   private static final long serialVersionUID = -6669530557926742097L;

   public ResourceIOException()
   {
   }

   public ResourceIOException(final String message)
   {
      super(message);
   }

   public ResourceIOException(final String message, final Throwable cause)
   {
      super(message, cause);
   }

   public ResourceIOException(final Throwable cause)
   {
      super(cause);
   }
}
