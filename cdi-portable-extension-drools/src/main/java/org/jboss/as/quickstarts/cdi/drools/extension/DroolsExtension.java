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

import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.jboss.as.quickstarts.model.LineItem;

public class DroolsExtension implements Extension {
    private final Logger log = Logger
        .getLogger(DroolsExtension.class.getName());

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
        log.info("beginning the scanning process");
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        log.info("finished the scanning process");
    }

    /*
     * ProcessInjectionTarget raises when a managed bean is injected Every times a managedbean annotated with @FactModel it
     * delegates LineItemInjectionTarget to add custom behaviours
     */
    <X extends LineItem> void processInjectionTarget(
        @Observes ProcessInjectionTarget<X> pit) {
        Class<? extends LineItem> klass = pit.getAnnotatedType().getJavaClass();
        log.info("Setting up injection target for " + klass);
        if (pit.getAnnotatedType().isAnnotationPresent(FactModel.class)) {
            pit.setInjectionTarget(new LineItemInjectionTarget<X>(pit
                .getInjectionTarget()));
        }
    }

    /*
     * Before the container builds the metamodel of a class annotated with
     * 
     * @FactModel this method add an annotation @FactMethodBinding on every setter. It's possible thank to deltaspike
     * AnnotatedTypeBuilder that aids to add annotations to class methods and parameter. Using deltaspike AnnotatedTypeBuilder
     * and interceptors you can have a deep control on bean you want to manage.
     */
    <X extends LineItem> void manageFactModel(
        @Observes ProcessAnnotatedType<X> pat) {
        if (pat.getAnnotatedType().isAnnotationPresent(FactModel.class)) {
            for (AnnotatedMethod<? super X> m : pat.getAnnotatedType()
                .getMethods()) {
                if (m.getJavaMember().getName().startsWith("set")) {
                    AnnotatedTypeBuilder<X> builder = new AnnotatedTypeBuilder<X>()
                        .readFromType(pat.getAnnotatedType()).addToMethod(
                            m,
                            AnnotationInstanceProvider
                                .of(FactMethodBinding.class));
                    pat.setAnnotatedType(builder.create());
                }

            }

        }
    }

}
