/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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

import javax.enterprise.inject.Alternative;

/**
 * Alternative implementation for Tax
 * 
 * @author Nevin Zhu
 * 
 */
@Alternative
public class TaxImpl_2 implements Tax {

    @Override
    public String getRate() {
        // TODO Auto-generated method stub
        return "Tax_2 Rate! To switch back to the default, go to /META-INF/beans.xml and comment out the 'alternatives' tag";
    }

}
