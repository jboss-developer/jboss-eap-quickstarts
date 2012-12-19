/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.quickstart.deltaspike.beanbuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.jboss.as.quickstart.deltaspike.beanbuilder.model.Person;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ByIdExtension implements Extension {

    // All nicks that needs to be found
    private List<String> ids = new LinkedList<String>();

    // A List of one contextualLifecycle for each Bean/Nick
    private List<PersonContextualLifecycle> contextualLifecycles = new ArrayList<PersonContextualLifecycle>();

    /**
     * This method is fired for every component class supporting injection that may be instantiated by the container at runtime.
     * 
     * It will look for all injection target and collect all nicks that were used and needs to be found on {@link EntityManager}
     * 
     * @param pit
     */
    public <X extends Object> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit) {
        for (InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints()) {
            ById idValue = ip.getAnnotated().getAnnotation(ById.class);
            if (idValue != null) {
                // Store the nick value
                ids.add(idValue.value());
            }
        }
    }

    /**
     * This method is fired when CDI has fully completed the bean discovery process
     * 
     */
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        // final EntityManager em = getEntityManager(bm);
        for (final String idValue : ids) {
            // Create a ContextualLifecyle for each Nick found and store its reference for later injection of EntityManager
            PersonContextualLifecycle contextualLifecycle = new PersonContextualLifecycle(idValue);
            contextualLifecycles.add(contextualLifecycle);

            // Create a Bean using the Nick Qualifier with the right nick value and the contextualLifecycle previously created
            BeanBuilder<Person> beanBuilder = new BeanBuilder<Person>(bm)
                    .beanClass(Person.class)
                    .types(Person.class, Object.class)
                    // The qualifier with its value
                    .qualifiers(new ByIdLiteral(idValue))
                    // The contextualLifecycle previously created
                    .beanLifecycle(contextualLifecycle);
            // Create and add the Bean
            abd.addBean(beanBuilder.create());
        }
    }

    /**
     * This method is fired after the container has validated that there are no deployment problems and before creating contexts
     * or processing requests.
     * 
     * Here we can get the {@link EntityManager} reference and set it on {@link PersonContextualLifecycle}
     * 
     */
    public void AfterDeploymentValidation(@Observes AfterDeploymentValidation adv, BeanManager bm) {
        // Retrieve EntityManager Reference
        EntityManager em = getEntityManager(bm);
        // Add it to every PersonContextualLifecycle previously created
        for (PersonContextualLifecycle lifecycle : contextualLifecycles) {
            lifecycle.setEntityManager(em);
        }
    }
    
    /**
     * Uses the {@link BeanManager} to get the {@link EntityManager} reference 
     */
    @SuppressWarnings("rawtypes")
    private EntityManager getEntityManager(BeanManager bm) {
        Set<Bean<?>> beans = bm.getBeans(EntityManager.class);
        Bean<?> bean = bm.resolve(beans);
        CreationalContext cc = bm.createCreationalContext(bean);
        return (EntityManager) bm.getReference(bean, EntityManager.class, cc);
    }


    /**
     * This class represents the {@link ById} annotation with its value. It is used by the {@link BeanBuilder} to set the
     * {@link Bean} {@link Qualifier}
     * 
     */
    public static class ByIdLiteral extends AnnotationLiteral<ById> implements ById {

        private static final long serialVersionUID = 1L;

        private String value;

        public ByIdLiteral(String v) {
            this.value = v;
        }

        @Override
        public String value() {
            return value;
        }

    }

    /**
     * This is the {@link ContextualLifecycle} that is used to by the {@link Bean} to create instances of {@link Person}.
     * 
     * It uses the {@link EntityManager#find(Class, Object)} to query the {@link Person} from Database
     * 
     */
    public static class PersonContextualLifecycle implements ContextualLifecycle<Person> {

        private EntityManager em;
        private String idValue;

        public PersonContextualLifecycle(String idValue) {
            this.idValue = idValue;
        }

        public void setEntityManager(EntityManager em) {
            this.em = em;
        }

        @Override
        public void destroy(Bean<Person> bean, Person instance, CreationalContext<Person> creationalContext) {
            creationalContext.release();
        }

        @Override
        public Person create(Bean<Person> bean, CreationalContext<Person> creationalContext) {
            // Here we use the entityManager to get the Person Instance
            return em.find(Person.class, idValue);
        }
    }

}
