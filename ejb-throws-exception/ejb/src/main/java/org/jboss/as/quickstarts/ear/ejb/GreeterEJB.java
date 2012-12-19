package org.jboss.as.quickstarts.ear.ejb;

import javax.ejb.Stateless;
import org.jboss.as.quickstarts.ear.client.GreeterEJBLocal;
import org.jboss.as.quickstarts.ear.client.GreeterException;

/**
 * @author bmaxwell
 * Session Bean implementation class GreeterEJB
 */
@Stateless
public class GreeterEJB implements GreeterEJBLocal {

    /**
     * Default constructor. 
     */
    public GreeterEJB() {
    }
    
    public String sayHello(String name) throws GreeterException {
        
        if(name == null || name.equals(""))
            throw new GreeterException("name cannot be null or empty");
        
        return "Hello " + name;        
    }

}
