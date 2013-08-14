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

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import org.jboss.as.quickstarts.cdi.drools.extension.RuleSession;
import org.jboss.as.quickstarts.cdi.drools.extension.RuleSessionManager;

/*
 * The producer manages the ksession's scope.
 * Here the ksession's scope is  ConversationScoped: 
 * two different tabs on browser
 * mean two different ksession without interferences.
 * 
 * 
 * ksession rules are defined by the producer.
 * 
 */

public class RuleSessionProducer {
    @Produces
    @RuleSession
    @ConversationScoped
    RuleSessionManager getRuleSessionManager() {
        RuleSessionManager toRet = new RuleSessionManager();
        toRet.setupKsession("SimpleRule.drl");
        return toRet;
    }

}
