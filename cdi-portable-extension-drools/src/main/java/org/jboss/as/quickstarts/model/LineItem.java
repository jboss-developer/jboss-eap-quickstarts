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

import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.as.quickstarts.cdi.drools.extension.FactModel;
import org.jboss.as.quickstarts.cdi.drools.extension.RuleSession;
import org.jboss.as.quickstarts.cdi.drools.extension.RuleSessionManager;

/*
 * A Class annoted as @FactModel
 * is managed during injection by DroolsExtension.java and LineItemInjectionTarget.java
 * so that its instances are added to ksession as a fact.
 * Ksession is an injected field of the @FactModel class in this way developer has deeper control on ksession
 * 
 * Setters method are annotated as @FactMethodBinding by
 * DroolsExtension.java and an interceptor is attached to them.
 * In this way when the object is updated, its copy in ksession is updated too.
 */
@Named
@FactModel
public class LineItem {

    @Inject
    @RuleSession
    RuleSessionManager rsession;
    private Double value;
    private String description;
    private double discount = 0d;
    private boolean inBasket = false;

    public boolean getInBasket() {
        return inBasket;
    }

    public void setInBasket(boolean inBasket) {
        this.inBasket = inBasket;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void dispose() {
        this.rsession.removeMe(this);
    }

}
