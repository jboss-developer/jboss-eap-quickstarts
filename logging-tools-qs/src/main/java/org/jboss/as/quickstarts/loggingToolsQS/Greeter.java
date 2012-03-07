package org.jboss.as.quickstarts.loggingToolsQS;




/**
 * A simple CDI bean which is able to say hello in different languages
 * 
 * @author dmison@me.com
 * 
 */

public class Greeter 
{

   String sayHello(String name) 
   {	
      return "Hello " + name + "!";
   }



}
