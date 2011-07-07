/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package com.mycompany.subsystem.handlers;

import java.util.List;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

/**
 * Handler responsible for adding the subsystem child resource to the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class SubsystemChildAdd extends AbstractBoottimeAddStepHandler {

    public static final SubsystemChildAdd INSTANCE = new SubsystemChildAdd();

    private SubsystemChildAdd() {
    }

    /** {@inheritDoc} */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        //We need to initialize the model with the expected elements here

        ////////////////////////////////////////////
        // Children

        //This resource does not have any children


        ////////////////////////////////////////////
        //Attributes

        //This resource has an optional boolean attribute called 'enabled', we can set it as follows:
        //Default to 'true' if not passed in as part of the parameters
        boolean enabled = operation.get("enabled").asBoolean(true);
        //And then set it in the model!
        model.get("enabled").set(enabled);

        //Or if the attribute was compulsory we would do (the require() method throws
        //an error if there is no 'enabled' attribute in the operation)
        //boolean enabled = operation.require("enabled");
        //model.get("enabled").set(enabled);
    }

    /** {@inheritDoc} */
    @Override
    public void performBoottime(OperationContext context, ModelNode operation, ModelNode model,
            ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
            throws OperationFailedException {
        //Don't do anything
    }
}
