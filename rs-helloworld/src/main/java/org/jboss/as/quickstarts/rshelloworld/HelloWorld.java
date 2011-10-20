package org.jboss.as.quickstarts.rshelloworld;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class HelloWorld {
   @Inject
   HelloService helloService;

	private static final Logger log = Logger.getLogger(HelloWorld.class.getName());
	
	@GET
    @Path("/json")
	@Produces({ "application/json" })
    public String getHelloWorldJSON(){
		log.log(Level.ALL, "Llegó");
        return "{result:\""+helloService.createHelloMessage("World")+"\"}";
    }
	
	@GET
    @Path("/xml")
	@Produces({ "application/xml" })
    public String getHelloWorldXML(){
		log.log(Level.ALL, "Llegó");
        return "<xml><result>"+helloService.createHelloMessage("World")+"</result></xml>";
    }
	
}
