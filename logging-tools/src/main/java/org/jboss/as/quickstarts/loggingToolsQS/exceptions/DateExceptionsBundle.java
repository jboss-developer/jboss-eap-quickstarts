/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.loggingToolsQS.exceptions;

import java.text.ParseException;

import org.jboss.logging.Message;
import org.jboss.logging.Messages;
import org.jboss.logging.Param;

@org.jboss.logging.MessageBundle(projectCode = "GRTDATES")
public interface DateExceptionsBundle {
    DateExceptionsBundle EXCEPTIONS = Messages.getBundle(DateExceptionsBundle.class);

    @Message(id = 7, value = "The date you sent me isn't valid, '%s'.  Sorry.")
    ParseException targetDateStringDidntParse(String dateString, @Param int errorOffset);

}
