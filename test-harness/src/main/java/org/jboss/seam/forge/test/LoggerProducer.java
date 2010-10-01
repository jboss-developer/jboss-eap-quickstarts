package org.jboss.seam.forge.test;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.weld.extensions.log.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerProducer
{
   @Produces
   public Logger produceLog(final InjectionPoint injectionPoint)
   {
      if (injectionPoint.getAnnotated().isAnnotationPresent(Category.class))
      {
         String category = injectionPoint.getAnnotated().getAnnotation(Category.class).value();
         return LoggerFactory.getLogger(category);
      }
      else
      {
         return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
      }
   }
}