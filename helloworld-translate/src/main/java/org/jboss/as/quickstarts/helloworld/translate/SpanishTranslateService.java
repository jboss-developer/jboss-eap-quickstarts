package org.jboss.as.quickstarts.helloworld.translate;

import org.jboss.as.quickstarts.helloworld.translate.qualifier.Spanish;

/**
 * A simple implementation for the Spanish language.
 *
 * The {@link Spanish} qualifier tells CDI this is a special instance of
 * the {@link TranslateService}.
 *
 * @author Jason Porter
 */
@Spanish
public class SpanishTranslateService implements TranslateService {

   @Override
   public String hello() {
     return "Hola";
   }

}
