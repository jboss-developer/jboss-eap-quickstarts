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

package org.jboss.seam.forge.parser.java.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.jboss.seam.forge.parser.java.Annotation;
import org.jboss.seam.forge.parser.java.AnnotationTarget;
import org.jboss.seam.forge.parser.java.impl.AnnotationImpl;
import org.jboss.seam.forge.parser.java.util.TypesNames;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class AnnotationAccessor
{

   @SuppressWarnings("unchecked")
   public Annotation addAnnotation(final AnnotationTarget<?> target, final BodyDeclaration body)
   {
      Annotation annotation = new AnnotationImpl(target);
      body.modifiers().add(0, annotation.getInternal());
      return annotation;
   }

   public Annotation addAnnotation(final AnnotationTarget<?> target, final BodyDeclaration body, final Class<?> clazz)
   {
      return addAnnotation(target, body, clazz.getName());
   }

   public Annotation addAnnotation(final AnnotationTarget<?> target, final BodyDeclaration body, final String className)
   {
      return addAnnotation(target, body).setName(className);
   }

   public List<Annotation> getAnnotations(final AnnotationTarget<?> target, final BodyDeclaration body)
   {
      List<Annotation> result = new ArrayList<Annotation>();

      List<?> modifiers = body.modifiers();
      for (Object object : modifiers)
      {
         if (object instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            Annotation annotation = new AnnotationImpl(target, object);
            result.add(annotation);
         }
      }

      return Collections.unmodifiableList(result);
   }

   public <T extends AnnotationTarget<?>> T removeAnnotation(final T target, final BodyDeclaration body,
            final Annotation annotation)
   {
      List<?> modifiers = body.modifiers();
      for (Object object : modifiers)
      {
         if (object.equals(annotation.getInternal()))
         {
            modifiers.remove(object);
            break;
         }
      }
      return target;
   }

   public <T extends AnnotationTarget<?>> boolean hasAnnotation(final T target, final BodyDeclaration body, String type)
   {
      List<?> modifiers = body.modifiers();
      for (Object object : modifiers)
      {
         if (object instanceof org.eclipse.jdt.core.dom.Annotation)
         {
            Annotation annotation = new AnnotationImpl(target, object);
            String annotationType = annotation.getName();
            if (TypesNames.areEquivalent(type, annotationType))
            {
               return true;
            }
         }
      }
      return false;
   }
}
