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
package org.jboss.as.quickstarts.cdi.drools.extension;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/*
 * The RuleSessionManager helps to manage 
 * ksession method as add, retract, trigger rules
 */
public class RuleSessionManager implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3443893518551799090L;
    private final static Logger log = Logger.getLogger(RuleSessionManager.class
        .getName());
    private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;

    public StatefulKnowledgeSession getKsession() {
        return this.ksession;
    }

    /*
     * This is a basic knowledge session: rules are loaded by a file: resources/SimpleRule.drl It's possible to load rule from
     * an url or a repository.
     */

    public void setupKsession(String ruleFile) {
        try {
            // load up the knowledge base
            kbase = readKnowledgeBase(ruleFile);
            ksession = kbase.newStatefulKnowledgeSession();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /*
     * This methods is called by the interceptor around setters methods calls on fact-bean It synchronizes java bean and drools
     * fact
     */
    public void updateMe(Object fact) {
        ksession.update(ksession.getFactHandle(fact), fact);
    }

    /*
     * Every injection of a @FactModel calls this method to add the fact in the ksession
     */
    public void addFact(@FactModel Object fact) {
        ksession.insert(fact);

    }

    /*
     * Disposing an object will remove it from ksession
     */
    public void removeMe(Object fact) {
        ksession.retract(ksession.getFactHandle(fact));

    }

    public void closeKsession() {
        try {
            ksession.dispose();
        } catch (Throwable t) {
            log.log(Level.SEVERE, t.getMessage());
        }
    }

    /*
     * it executes all rules upon the facts inserted in the ksession Rules can modify the facts.
     */
    public void fireRule() {
        this.ksession.fireAllRules();

    }

    private KnowledgeBase readKnowledgeBase(String ruleFile) throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
            .newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource(ruleFile),
            ResourceType.DRL);
        hasErrors(kbuilder);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        hasErrors(kbuilder);
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }

    private static void hasErrors(KnowledgeBuilder kbuilder) throws Exception {
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                log.log(Level.SEVERE, error.getMessage());
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

    }

}
