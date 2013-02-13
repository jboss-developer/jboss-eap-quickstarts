/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.model;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.cdi.drools.extension.FactModel;

/*
 * This manager is the handler behind
 * the main view.
 * It produces basketItem triggering 
 * postConstruct method in LineItemInjection class.
 *
 */

@Model
public class Manager {

    @Inject
    private OrderBuilder ob;

    @Inject
    private BeanManager beanManager;

    /*
     * This injection creates an instance of lineItem and adds it to its ksession. Refer to DroolsExtension.java and
     * LineItemInjectionTarget.java.
     */
    @Inject
    @FactModel
    private LineItem basketItem;

    @Produces
    @Named
    public LineItem getBasketItem() {
        return basketItem;
    }

    public void save() {
        basketItem.setInBasket(true);
        ob.addLineItem(basketItem);

    }

    public void clear(LineItem lineItem) {
        ob.removeItem(lineItem);
        basketItem.dispose();
    }

}
