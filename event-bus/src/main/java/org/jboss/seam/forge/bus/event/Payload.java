package org.jboss.seam.forge.bus.event;

import java.io.InputStream;

/**
 * A Payload represents an underlying file resource associated with the project, such as a Java file, XML file,
 * properties file or any other resource that is under the control of the system.
 *
 * @author Mike Brock <cbrock@redhat.com>
 */
public interface Payload<M>
{
   /**
    * Returns the name of the resource associated with the payload. Usually the fully-qualified filename.
    * @return Name of the resource.
    */
   public String getName();

   /**
    * Returns the type of payload. (eg. "java", "xml", "resource-bundle", etc.)
    * @return a String representation of the resource.
    */
   public String getType();

   /**
    * Returns an inputstream of the underlying resource.
    * @return an InputStream to the underlying resource
    */
   public InputStream getResourceAsStream();

   /**
    * Returns the Model object associated with the Payload.
    * @return The underlying API that represents the model for the payload.
    */
   public M getModel();
}
