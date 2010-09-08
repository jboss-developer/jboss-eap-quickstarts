/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.seam.sidekick.bus.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedCallable;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Singleton;

import org.jboss.seam.sidekick.bus.event.BaseEvent;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ObserverCaptureExtension implements Extension
{
   private final Map<Class<?>, List<BusManaged>> eventQualifierMap = new HashMap<Class<?>, List<BusManaged>>();
   private int rollingIdentifier = 0;

   public void scan(@Observes final ProcessAnnotatedType<Object> event)
   {
      AnnotatedType<Object> originalType = event.getAnnotatedType();
      AnnotatedType<Object> newType = originalType;
      List<AnnotatedMethod> obsoleteMethods = new ArrayList<AnnotatedMethod>();
      List<AnnotatedMethod> replacementMethods = new ArrayList<AnnotatedMethod>();

      for (AnnotatedMethod<?> method : getOrderedMethods(originalType))
      {
         for (AnnotatedParameter<?> param : method.getParameters())
         {
            if (param.isAnnotationPresent(Observes.class))
            {
               if (param.getTypeClosure().contains(BaseEvent.class))
               {
                  replacementMethods.add(qualifyObservedEvent(method, param));
                  obsoleteMethods.add(method);
               }
            }
         }
      }

      newType = removeMethodsFromType(originalType, obsoleteMethods);
      newType = addReplacementMethodsToType(newType, replacementMethods);

      event.setAnnotatedType(newType);
   }

   private List<AnnotatedMethod<? super Object>> getOrderedMethods(AnnotatedType<Object> originalType)
   {
      Set<AnnotatedMethod<? super Object>> methods = originalType.getMethods();

      List<AnnotatedMethod<? super Object>> result = new ArrayList<AnnotatedMethod<? super Object>>();
      result.addAll(methods);

      Collections.sort(result, new Comparator<Object>()
      {
         @Override
         public int compare(Object left, Object right)
         {
            String lid = ((AnnotatedMethod<? super Object>) left).getJavaMember().toGenericString();
            String rid = ((AnnotatedMethod<? super Object>) right).getJavaMember().toGenericString();
            return lid.compareTo(rid);
         }
      });

      return result;
   }

   private AnnotatedType<Object> removeMethodsFromType(final AnnotatedType type,
            final List<AnnotatedMethod> targetedMethods)
   {

      final Set<AnnotatedMethod> methods = new HashSet<AnnotatedMethod>();
      methods.addAll(type.getMethods());
      methods.removeAll(targetedMethods);

      return new AnnotatedType()
      {
         @Override
         public Class getJavaClass()
         {
            return type.getJavaClass();
         }

         @Override
         public Set<AnnotatedConstructor> getConstructors()
         {
            return type.getConstructors();
         }

         @Override
         public Set<AnnotatedMethod> getMethods()
         {
            return methods;
         }

         @Override
         public Set<AnnotatedField> getFields()
         {
            return type.getFields();
         }

         @Override
         public Type getBaseType()
         {
            return type.getBaseType();
         }

         @Override
         public Set<Type> getTypeClosure()
         {
            return type.getTypeClosure();
         }

         @Override
         public <T extends Annotation> T getAnnotation(final Class<T> annotationType)
         {
            return type.getAnnotation(annotationType);
         }

         @Override
         public Set<Annotation> getAnnotations()
         {
            return type.getAnnotations();
         }

         @Override
         public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType)
         {
            return type.isAnnotationPresent(annotationType);
         }
      };
   }

   private AnnotatedType<Object> addReplacementMethodsToType(final AnnotatedType newType,
            final List<AnnotatedMethod> replacementMethods)
   {
      newType.getMethods().addAll(replacementMethods);
      return newType;
   }

   private AnnotatedMethod<Object> qualifyObservedEvent(
            final AnnotatedMethod method, final AnnotatedParameter param)
   {
      final List<AnnotatedParameter> parameters = new ArrayList<AnnotatedParameter>();

      parameters.addAll(method.getParameters());
      parameters.remove(param);
      parameters.add(addUniqueQualifier(method, param));

      return new AnnotatedMethod()
      {
         @Override
         public List<AnnotatedParameter> getParameters()
         {
            return parameters;
         }

         @Override
         public AnnotatedType<Object> getDeclaringType()
         {
            return method.getDeclaringType();
         }

         @Override
         public boolean isStatic()
         {
            return method.isStatic();
         }

         @Override
         public <T extends Annotation> T getAnnotation(final Class<T> annotation)
         {
            return method.getAnnotation(annotation);
         }

         @Override
         public Set<Annotation> getAnnotations()
         {
            return method.getAnnotations();
         }

         @Override
         public Type getBaseType()
         {
            return method.getBaseType();
         }

         @Override
         public Set<Type> getTypeClosure()
         {
            return method.getTypeClosure();
         }

         @Override
         public boolean isAnnotationPresent(final Class<? extends Annotation> annotation)
         {
            return method.isAnnotationPresent(annotation);
         }

         @Override
         public Method getJavaMember()
         {
            return method.getJavaMember();
         }
      };
   }

   private AnnotatedParameter addUniqueQualifier(final AnnotatedMethod method,
            final AnnotatedParameter param)
   {
      final String identifier = String.valueOf(rollingIdentifier++);
      final String methodName = method.getJavaMember().getName();
      final BusManaged qualifier = new BusManaged()
      {
         @Override
         public Class<? extends Annotation> annotationType()
         {
            return BusManaged.class;
         }

         @Override
         public String value()
         {
            return identifier;
         }

         @Override
         public String method()
         {
            return methodName;
         }
      };

      addQualifierToMap(method, param, qualifier);

      final Set<Annotation> annotations = new HashSet<Annotation>();
      annotations.addAll(param.getAnnotations());
      annotations.add(qualifier);

      return new AnnotatedParameter<Object>()
      {
         @Override
         public <T extends Annotation> T getAnnotation(final Class<T> clazz)
         {
            if (BusManaged.class.isAssignableFrom(clazz))
            {
               return (T) qualifier;
            }
            return param.getAnnotation(clazz);
         }

         @Override
         public Set<Annotation> getAnnotations()
         {
            return annotations;
         }

         @Override
         public Type getBaseType()
         {
            return param.getBaseType();
         }

         @Override
         public Set<Type> getTypeClosure()
         {
            return param.getTypeClosure();
         }

         @Override
         public boolean isAnnotationPresent(final Class<? extends Annotation> clazz)
         {
            if (BusManaged.class.isAssignableFrom(clazz))
            {
               return true;
            }
            return param.isAnnotationPresent(clazz);
         }

         @Override
         public AnnotatedCallable<Object> getDeclaringCallable()
         {
            return param.getDeclaringCallable();
         }

         @Override
         public int getPosition()                                          
         {
            return param.getPosition();
         }
      };
   }

   private void addQualifierToMap(final AnnotatedMethod annotatedMethod,
            final AnnotatedParameter param, final BusManaged qualifier)
   {
      Method method = annotatedMethod.getJavaMember();
      Class<?> clazz = method.getParameterTypes()[param.getPosition()];
      List<BusManaged> qualifiers = eventQualifierMap.get(clazz);
      if (qualifiers == null)
      {
         qualifiers = new ArrayList<BusManaged>();
      }
      qualifiers.add(qualifier);
      eventQualifierMap.put(clazz, qualifiers);
   }

   /**
    * Return a list containing instances of {@link BusManaged} annotations
    * corresponding to the given event type.
    */
   public List<BusManaged> getEventQualifiers(final Class<? extends BaseEvent> clazz)
   {
      List<BusManaged> result = new ArrayList<BusManaged>();
      for (Entry<Class<?>, List<BusManaged>> entry : eventQualifierMap.entrySet())
      {
         Class<?> key = entry.getKey();
         List<BusManaged> value = entry.getValue();
         if (key.isAssignableFrom(clazz))
         {
            result.addAll(value);
         }
      }
      return result;
   }

   /**
    * Return the entire map of Event Types and their corresponding lists of
    * {@link BusManaged} annotation instances. This map can be used to implement
    * a strategy for custom firing of events.
    */
   public Map<Class<?>, List<BusManaged>> getEventQualifierMap()
   {
      return eventQualifierMap;
   }
}
