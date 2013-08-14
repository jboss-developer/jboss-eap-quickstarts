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

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/*
 * 
 * This business object lives inside a conversation scope.
 * As a shopping basket it manages actions of adding and removing
 * items triggering centralized rules that work on the complete collection of basketItems.
 * It acts as a major-controller inside a conversationScoped, on actions  that happen 
 * on a minor-controller, class Manager, inside the request scope.
 */
@ConversationScoped
@Stateful
public class OrderBuilder {
    private ArrayList<LineItem> order;
    private @Inject
    Conversation conversation;

    @Produces
    @Named
    public ArrayList<LineItem> getOrder() {
        return order;
    }

    @PostConstruct
    public void createOrder() {
        order = new ArrayList<LineItem>();
        conversation.begin();
    }

    public void addLineItem(LineItem lineItem) {
        order.add(lineItem);
        lineItem.rsession.fireRule();
    }

    public void removeItem(LineItem lineItem) {
        order.remove(lineItem);
        lineItem.dispose();
        lineItem.rsession.fireRule();
    }

    public void purchase() {
        conversation.end();
    }

    @Remove
    public void destroy() {
    }

}
