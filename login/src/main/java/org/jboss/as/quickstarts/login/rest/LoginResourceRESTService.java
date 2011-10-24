package org.jboss.as.quickstarts.login.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.as.quickstarts.login.AuthenticationRequired;
import org.jboss.as.quickstarts.login.User;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/logins")
@AuthenticationRequired
@Stateless
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

    //This should really be @POST
    //but for the sake of easy demoing we are using @GET
    @GET
    @Path("/add/{username}")
    @Produces("text/xml")
    public User addUser(@PathParam("username") String username, @QueryParam("name") String name, @QueryParam("password") String password) {
        final User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setUsername(username);
        em.persist(user);
        return user;
    }

}
