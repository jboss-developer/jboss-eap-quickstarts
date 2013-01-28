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
package org.jboss.as.quickstarts.cdi.alternative;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.jboss.as.quickstarts.cdi.alternative.Tax;

/**
 * <p>
 * EJB Bean Implementation. 
 * 
 * the stateTax attribute will be injected during run time. 
 * If beans.xml has class Washington defined as an alternative, then that class will be injected.  
 * Otherwise, the default California is injected. 
 * </p>
 * 
 * @author Nevin Zhu
 * 
 */
public @Singleton class TaxBean implements Tax {
    @Inject
    private StateTax stateTax;
    
    public String getRate() {
        return stateTax.getRate();
    }

}
