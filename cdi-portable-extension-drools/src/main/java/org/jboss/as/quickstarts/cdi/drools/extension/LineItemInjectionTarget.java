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

import java.lang.reflect.Field;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

import org.jboss.as.quickstarts.model.LineItem;

public class LineItemInjectionTarget<X extends LineItem> implements
    InjectionTarget<X> {
    private final InjectionTarget<X> wrapped;

    public LineItemInjectionTarget(InjectionTarget<X> it) {
        wrapped = it;

    }

    @Override
    public void inject(X instance, CreationalContext<X> ctx) {
        wrapped.inject(instance, ctx);

    }

    /*
     * After the construction of a managed bean, this method adds the item to ksession. In this way the managed bean becomes a
     * fact and the rule engine can apply rules to it.
     */
    @Override
    public void postConstruct(X instance) {
        wrapped.postConstruct(instance);
        Set<InjectionPoint> s = this.getInjectionPoints();
        for (InjectionPoint current : s) {
            Field field = (java.lang.reflect.Field) current.getMember();
            if (field.getAnnotation(RuleSession.class) != null) {
                try {
                    ((RuleSessionManager) field.get(instance))
                        .addFact(instance);
                } catch (IllegalArgumentException e) {
                    throw new InjectionException(
                        "Cannot add fact, illegal arg ");

                } catch (IllegalAccessException e) {
                    throw new InjectionException("Cannot add fact ");
                }

            }
        }
    }

    @Override
    public void preDestroy(X instance) {
        wrapped.preDestroy(instance);
    }

    @Override
    public X produce(CreationalContext<X> ctx) {
        return wrapped.produce(ctx);
    }

    @Override
    public void dispose(X instance) {
        wrapped.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return wrapped.getInjectionPoints();
    }
}
