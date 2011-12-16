package org.jboss.as.quickstarts.helloworld;

import javax.inject.Inject;

import org.jboss.as.quickstarts.helloworld.translate.TranslateService;
import org.jboss.as.quickstarts.helloworld.translate.qualifier.Spanish;

/**
 * A simple CDI service which is able to say hello to someone
 * using the specified {@link TranslateService}.
 *
 * @author Pete Muir
 * @author Jason Porter
 */
public class HelloService {

   /**
    * The {@link Spanish} qualifier is extra metadata about the injection point
    * CDI uses to determine the one (and only) correct instance for injection.
    *
    * {@link Inject} is the annotation CDI uses to create an injection point - a
    * resource CDI will inject.
    */
   @Inject
   @Spanish
   private TranslateService translateService;

   String createHelloMessage(String name) {
      return translateService.hello() + " " + name + "!";
   }

}
