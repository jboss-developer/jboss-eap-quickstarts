/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat, Inc. and/or its affiliates, and individual
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

package org.jboss.security.negotiation.toolkit;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

/**
 * Utility to be called from the command line to verify that it is possible
 * to authenticate against the KDC when providing the keytab.
 * 
 * @author darran.lofthouse@jboss.com
 * @version $Revision: 70489 $
 */
public class KeytabPasswordLogon
{

   /**
    * Utility entry point.
    */
   public static void main(String[] args) throws Exception
   {      
      System.out.println(" * * KeytabPasswordLogin * *");
      LoginContext login = new LoginContext("KeytabPasswordSample");
      login.login();

      System.out.println("Authenticated");

      Subject subject = login.getSubject();

      System.out.println("Subject - " + String.valueOf(subject));
   }
}
