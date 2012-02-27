package org.jboss.as.quickstarts.tasksJsf;

import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * Provides common resources used across whole application
 * 
 * @author Lukas Fryc
 * 
 */
@Stateful
@RequestScoped
public class Resources {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Produces
    public EntityManager getEm() {
        return em;
    }

    @Produces
    public Logger getLogger(InjectionPoint ip) {
        String category = ip.getMember().getDeclaringClass().getName();
        return Logger.getLogger(category);
    }

    @Produces
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
