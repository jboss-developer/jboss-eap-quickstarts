/*
 * JBoss, by Red Hat.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
