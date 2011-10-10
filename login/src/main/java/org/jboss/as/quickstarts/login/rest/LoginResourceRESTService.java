package org.jboss.as.quickstarts.login.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.as.quickstarts.login.Login;
import org.jboss.as.quickstarts.login.User;

/**
 * JAX-RS Example
 *
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/logins")
@RequestScoped
public class LoginResourceRESTService {
   @Inject
   private EntityManager em;

   @GET
   @Produces("text/xml")
   public List<User> listAllMembers() {
      @SuppressWarnings("unchecked")
      final List<User> results = em.createQuery("select u from User u order by u.username").getResultList();
      return results;
   }

   @GET
   @Path("/{id:\\w*}")
   @Produces("text/xml")
   public User lookupUserByUsername(@PathParam("id") String id) {
      return em.find(User.class, id);
   }
}
