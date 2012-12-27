/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.as.quickstarts.kitchensink.exception;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.jboss.as.quickstarts.kitchensink.exception.annotation.RestRequestLiteral;

/**
 * <p>
 * This exception mapper is registered with JAX-RS by the provider annotation. It
 * </p>
 * <p>
 * You can inject other CDI beans into it.
 * </p>
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Provider
public class DeltaSpikeRestExceptionMapper implements ExceptionMapper<Exception> {

    // Inject CDI Event
    @Inject
    private Event<ExceptionToCatchEvent> catchEvent;

    @Inject
    private Instance<Response> response;

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(Exception exception) {
        catchEvent.fire(new ExceptionToCatchEvent(exception, RestRequestLiteral.INSTANCE));
        Response resp = response.get();
        return resp;
    }

}
